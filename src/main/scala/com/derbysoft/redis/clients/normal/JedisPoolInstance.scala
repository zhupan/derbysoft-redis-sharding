package com.derbysoft.redis.clients.normal

import redis.clients.jedis.JedisPool


object JedisPoolInstance {

  def apply(hostAndPortValue: String): JedisPool = {
    val hostAndPort = hostAndPortValue.split(":")
    JedisPoolConfig.maxActive = 20000
    JedisPoolConfig.maxIdle = 200
    JedisPoolConfig.minIdle = 20
    new JedisPool(JedisPoolConfigInstance(), hostAndPort(0), hostAndPort(1).toInt, JedisPoolConfig.timeout)
  }

}
