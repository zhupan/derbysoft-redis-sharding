package com.derbysoft.redis.clients.datesharding.core

import java.util.concurrent.ConcurrentHashMap
import com.derbysoft.redis.clients.normal.JedisClientPool

object JedisClientPoolFactory extends JedisClientPoolFactory {

  def apply(host: String): JedisClientPool = {
    get(host)
  }

}

class JedisClientPoolFactory {

  val all = new ConcurrentHashMap[String, JedisClientPool]

  def get(hostAndPort: String): JedisClientPool = {
    val pool = all.get(hostAndPort)
    if (pool != null) {
      return pool
    }
    all.put(hostAndPort, new JedisClientPool(hostAndPort))
    all.get(hostAndPort)
  }

}



