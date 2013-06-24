package com.derbysoft.redis.clients.normal

import redis.clients.jedis.{BinaryJedis, Jedis}


class JedisClientPool(hostAndPortValue: String) {

  private val pool = JedisPoolInstance(hostAndPortValue)

  def withClient[T](body: Jedis => T) = {
    val jedis = pool.getResource
    try {
      body(jedis)
    } finally {
      if (jedis != null && jedis.isConnected) {
        pool.returnResource(jedis.asInstanceOf[BinaryJedis])
      }
    }
  }

  def getResource: Jedis = {
    var jedis: Jedis = null
    try {
      jedis = pool.getResource
    } catch {
      case e: Exception => {
        if (jedis != null) {
          pool.returnBrokenResource(jedis.asInstanceOf[BinaryJedis])
          jedis = null
        }
        throw new RuntimeException(e)
      }
    }
    jedis
  }

  def returnResource(jedis: Jedis) {
    pool.returnResource(jedis.asInstanceOf[BinaryJedis])
  }

  def returnBrokenResource(jedis: Jedis) {
    pool.returnBrokenResource(jedis.asInstanceOf[BinaryJedis])
  }

}