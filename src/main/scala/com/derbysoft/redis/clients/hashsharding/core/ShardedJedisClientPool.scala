package com.derbysoft.redis.clients.hashsharding.core

import redis.clients.jedis.ShardedJedis

protected object ShardedJedisClientPool extends ShardedJedisClientPool

protected class ShardedJedisClientPool {

  val pool = HashShardedJedisPool()

  def withClient[T](body: ShardedJedis => T) = {
    val client = pool.getResource
    try {
      body(client)
    } finally {
      pool.returnResource(client)
    }
  }

  def getResource: ShardedJedis = {
    pool.getResource
  }

  def returnResource(jedis: ShardedJedis) = {
    pool.returnResource(jedis)
  }

}