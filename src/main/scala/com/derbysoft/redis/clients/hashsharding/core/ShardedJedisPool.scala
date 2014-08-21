package com.derbysoft.redis.clients.hashsharding.core

import java.util.regex.Pattern
import scala.collection.JavaConversions._

import org.apache.commons.pool.impl.GenericObjectPool

import redis.clients.util.Hashing
import redis.clients.util.Pool
import redis.clients.jedis.{ShardedJedis, JedisShardInfo}
import org.apache.commons.pool.{PoolableObjectFactory, BasePoolableObjectFactory}
import java.util.concurrent.Callable
import com.derbysoft.redis.util.ExecutorUtils

class ShardedJedisPool(poolConfig: GenericObjectPool.Config, factory: PoolableObjectFactory[JedisShardInfo]) extends Pool[ShardedJedis](poolConfig, factory) {

  def this(poolConfig: GenericObjectPool.Config, shards: java.util.List[JedisShardInfo]) {
    this(poolConfig, new ShardedJedisFactory[JedisShardInfo](shards, Hashing.MURMUR_HASH, null))
  }

  def this(poolConfig: GenericObjectPool.Config, shards: java.util.List[JedisShardInfo], algo: Hashing) {
    this(poolConfig, new ShardedJedisFactory[JedisShardInfo](shards, algo, null))
  }

  def this(poolConfig: GenericObjectPool.Config, shards: java.util.List[JedisShardInfo], algo: Hashing, keyTagPattern: Pattern) {
    this(poolConfig, new ShardedJedisFactory[JedisShardInfo](shards, algo, keyTagPattern))
  }
}

/**
 * PoolableObjectFactory custom impl.
 */
private class ShardedJedisFactory[T](shards: java.util.List[JedisShardInfo], algo: Hashing, keyTagPattern: Pattern) extends BasePoolableObjectFactory[T] {

  override def makeObject(): T = {
    new ShardedJedis(shards, algo, keyTagPattern).asInstanceOf[T]
  }

  override def destroyObject(obj: T) {
    try {
      if ((obj != null) && (obj.isInstanceOf[ShardedJedis])) {
        val shardedJedis = obj.asInstanceOf[ShardedJedis]
        val list = new java.util.ArrayList[Callable[Unit]]
        for (jedis <- shardedJedis.getAllShards) {
          list.add(new Callable[Unit] {
            def call() {
              try {
                try {
                  jedis.quit()
                } catch {
                  case e: Exception => println(e.getMessage, e)
                }
                jedis.disconnect()
              } catch {
                case e: Exception => println(e.getMessage, e)
              }
            }
          })
        }
        ExecutorUtils.batchExecute(list)
      }
    } catch {
      case e: Exception => {
        println(e.getMessage, e)
        retryDestroyObject(obj)
      }
    }
  }

  private def retryDestroyObject(obj: T) {
    val shardedJedis = obj.asInstanceOf[ShardedJedis]
    for (jedis <- shardedJedis.getAllShards) {
      if (jedis != null && jedis.isConnected) {
        try {
          try {
            jedis.quit()
          } catch {
            case e: Exception => println(e.getMessage, e)
          }
          jedis.disconnect()
        } catch {
          case e: Exception => println(e.getMessage, e)
        }
      }
    }
  }

  override def validateObject(obj: T): Boolean = {
    try {
      val shardedJedis = obj.asInstanceOf[ShardedJedis]
      val list = new java.util.ArrayList[Callable[Boolean]]
      for (redis <- shardedJedis.getAllShards) {
        list.add(new Callable[Boolean] {
          def call(): Boolean = {
            "PONG".equalsIgnoreCase(redis.ping())
          }
        })
      }
      !ExecutorUtils.batchExecute(list).contains(false)
    } catch {
      case e: Exception =>
        println(e.getMessage, e)
        retryValidateObject(obj)
    }
  }

  private def retryValidateObject(obj: T): Boolean = {
    try {
      val shardedJedis = obj.asInstanceOf[ShardedJedis]
      for (redis <- shardedJedis.getAllShards) {
        if (!"PONG".equals(redis.ping())) {
          return false
        }
      }
      true
    } catch {
      case e: Exception =>
        println(e.getMessage, e)
        false
    }
  }

}
