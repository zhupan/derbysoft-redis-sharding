package com.derbysoft.redis.clients.hashsharding.core

import java.util.List
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
      val list = new java.util.ArrayList[Callable[Unit]]
      for (jedis <- shardedJedis.getAllShards) {
        list.add(new Callable[Unit] {
          def call() {
            jedis.quit()
          }
        })
      }
      ExecutorUtils.batchExecute(list)
    }
  }

  override def validateObject(obj: T): Boolean = {
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
  }

}
