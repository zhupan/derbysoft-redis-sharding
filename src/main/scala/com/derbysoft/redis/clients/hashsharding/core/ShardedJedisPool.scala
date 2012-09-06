package com.derbysoft.redis.clients.hashsharding.core

import java.util.List
import java.util.regex.Pattern
import scala.actors._, Actor._


import org.apache.commons.pool.impl.GenericObjectPool

import redis.clients.util.Hashing
import redis.clients.util.Pool
import redis.clients.jedis.{Jedis, ShardedJedis, JedisShardInfo}
import org.apache.commons.pool.{PoolableObjectFactory, BasePoolableObjectFactory}
import collection.mutable.ListBuffer

class ShardedJedisPool(poolConfig: GenericObjectPool.Config, factory: PoolableObjectFactory[JedisShardInfo]) extends Pool[ShardedJedis](poolConfig, factory) {

  def this(poolConfig: GenericObjectPool.Config, shards: List[JedisShardInfo]) {
    this(poolConfig, new ShardedJedisFactory[JedisShardInfo](shards, Hashing.MURMUR_HASH, null))
  }

  def this(poolConfig: GenericObjectPool.Config, shards: List[JedisShardInfo], algo: Hashing) {
    this(poolConfig, new ShardedJedisFactory[JedisShardInfo](shards, algo, null))
  }

  def this(poolConfig: GenericObjectPool.Config, shards: List[JedisShardInfo], algo: Hashing, keyTagPattern: Pattern) {
    this(poolConfig, new ShardedJedisFactory[JedisShardInfo](shards, algo, keyTagPattern))
  }
}

/**
 * PoolableObjectFactory custom impl.
 */
private class ShardedJedisFactory[T](shards: List[JedisShardInfo], algo: Hashing, keyTagPattern: Pattern) extends BasePoolableObjectFactory[T] {

  override def makeObject(): T = {
    new ShardedJedis(shards, algo, keyTagPattern).asInstanceOf[T]
  }

  override def destroyObject(obj: T) {
    if ((obj != null) && (obj.isInstanceOf[ShardedJedis])) {
      val shardedJedis = obj.asInstanceOf[ShardedJedis]
      val redisActor = actor {
        receiveWithin(10) {
          case jedis: Jedis => {
            jedis.quit()
            jedis.disconnect()
          }
        }
      }
      val all = shardedJedis.getAllShards.iterator()
      while (all.hasNext) {
        redisActor ! all.next()
      }
    }
  }

  override def validateObject(obj: T): Boolean = {
    try {
      val shardedJedis = obj.asInstanceOf[ShardedJedis]
      val result = new ListBuffer[Boolean]
      val redisActor = actor {
        receiveWithin(20) {
          case jedis: Jedis => {
            jedis.ping() match {
              case value => {
                if (!"PONG".equals(value)) {
                  result += false
                } else {
                  result += true
                }
              }
            }
          }
          case _ => result += false
        }
      }
      val all = shardedJedis.getAllShards.iterator()
      while (all.hasNext) {
        redisActor ! all.next()
      }
      while (true) {
        if (result.contains(false)) {
          return false
        }
        if (shardedJedis.getAllShards.size == result.size) {
          if (result.contains(false)) {
            return false
          }
          return true
        }
        Thread.sleep(1)
      }
      return false
    } catch {
      case e: Exception => {
        println(e.getMessage)
        return false
      }
    }
  }

}
