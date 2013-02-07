package com.derbysoft.redis.clients.hashsharding.core

import redis.clients.jedis.JedisShardInfo
import com.derbysoft.redis.clients.normal.JedisPoolConfig

protected object JedisShardInfoInstance {

  def apply(hostAndPortValue: String): JedisShardInfo = {
    val hostAndPort = hostAndPortValue.split(":")
    new JedisShardInfo(hostAndPort(0), hostAndPort(1).toInt, JedisPoolConfig.timeout)
  }

}
