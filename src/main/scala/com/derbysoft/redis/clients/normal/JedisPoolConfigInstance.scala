package com.derbysoft.redis.clients.normal


object JedisPoolConfig {

  var minIdle = 1
  var maxIdle = 200
  var maxActive = -1
  var maxWait = -1l
  var timeout = 60000
  var testOnBorrow = true

  def init(testOnBorrow: Boolean) {
    this.init(minIdle, maxIdle, maxActive, maxWait, testOnBorrow, timeout)
  }

  def init(minIdle: Int, maxIdle: Int, maxActive: Int, maxWait: Long, testOnBorrow: Boolean) {
    this.init(minIdle, maxIdle, maxActive, maxWait, testOnBorrow, timeout)
  }

  def init(minIdle: Int, maxIdle: Int, maxActive: Int, maxWait: Long, testOnBorrow: Boolean, timeout: Int) {
    this.minIdle = minIdle
    this.maxIdle = maxIdle
    this.maxActive = maxActive
    this.maxWait = maxWait
    this.timeout = timeout
    this.testOnBorrow = testOnBorrow
  }

}

object JedisPoolConfigInstance {

  def apply(): redis.clients.jedis.JedisPoolConfig = {
    val poolConfig = new redis.clients.jedis.JedisPoolConfig
    poolConfig.setMaxActive(JedisPoolConfig.maxActive)
    poolConfig.setMinIdle(JedisPoolConfig.minIdle)
    poolConfig.setMaxIdle(JedisPoolConfig.maxIdle)
    poolConfig.setMaxWait(JedisPoolConfig.maxWait)
    poolConfig.setTestOnBorrow(JedisPoolConfig.testOnBorrow)
    poolConfig.setMinEvictableIdleTimeMillis(10 * 60 * 1000)
    poolConfig
  }

}
