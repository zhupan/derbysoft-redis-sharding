package com.derbysoft.redis.clients.hashsharding.core

import redis.clients.jedis.ShardedJedis

object ShardedJedisClientPool extends ShardedJedisClientPool

protected class ShardedJedisClientPool {

  var pool = HashShardedJedisPool()

  def rePool() {
    HashShardedJedisPool.initShards()
    pool = HashShardedJedisPool()
  }

  def withClient[T](body: ShardedJedis => T) = {
    var jedis: ShardedJedis = null
    try {
      jedis = pool.getResource
      body(jedis)
    } catch {
      case e: Exception => {
        if (jedis != null) {
          pool.returnBrokenResource(jedis)
          jedis = null
        }
        throw new RuntimeException(e)
      }
    } finally {
      if (jedis != null) {
        pool.returnResource(jedis)
      }
    }
  }

  def getResource: ShardedJedis = {
    pool.getResource
  }

  def returnResource(jedis: ShardedJedis) {
    pool.returnResource(jedis)
  }

}