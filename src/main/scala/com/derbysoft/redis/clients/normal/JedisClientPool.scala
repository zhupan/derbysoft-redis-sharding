package com.derbysoft.redis.clients.normal

import redis.clients.jedis.{BinaryJedis, Jedis}


class JedisClientPool(hostAndPortValue: String) {

  val pool = JedisPoolInstance(hostAndPortValue)

  def withClient[T](body: Jedis => T) = {
    var jedis: Jedis = null
    try {
      jedis = pool.getResource
      body(jedis)
    } catch {
      case e: Exception => {
        if (jedis != null) {
          pool.returnBrokenResource(jedis.asInstanceOf[BinaryJedis])
        }
        throw new RuntimeException(e)
      }
    }
    finally {
      if (jedis != null) {
        pool.returnResource(jedis.asInstanceOf[BinaryJedis])
      }
    }
  }

  def getResource: Jedis = {
    pool.getResource
  }

  def returnResource(jedis: Jedis) {
    pool.returnResource(jedis.asInstanceOf[BinaryJedis])
  }


}