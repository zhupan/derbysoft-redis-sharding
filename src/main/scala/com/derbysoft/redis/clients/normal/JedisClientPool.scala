package com.derbysoft.redis.clients.normal

import redis.clients.jedis.{BinaryJedis, Jedis}


class JedisClientPool(hostAndPortValue: String) {

  val pool = JedisPoolInstance(hostAndPortValue)

  def withClient[T](body: Jedis => T) = {
    val jedis = pool.getResource
    try {
      body(jedis)
    } finally {
      pool.returnResource(jedis.asInstanceOf[BinaryJedis])
    }
  }

  def getResource: Jedis = {
    pool.getResource
  }

  def returnResource(jedis: Jedis) = {
    pool.returnResource(jedis.asInstanceOf[BinaryJedis])
  }


}