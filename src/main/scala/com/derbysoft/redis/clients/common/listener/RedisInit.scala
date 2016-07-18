package com.derbysoft.redis.clients.common.listener

import java.util.Properties
import javax.servlet.ServletContextEvent

import com.derbysoft.redis.clients.ShardingRedis
import com.derbysoft.redis.clients.common.config.Hosts
import com.derbysoft.redis.clients.hashsharding.core.ShardedJedisClientPool
import com.derbysoft.redis.clients.normal.JedisPoolConfig

object RedisInit {

  val redisConfigName = "redis-sharding-config.properties"

  val redisHostsSizeKey = "redis.hosts.size"

  val redisHostsSortKey = "redis.hosts.sort"

  def apply(event: ServletContextEvent) {
    var configName = redisConfigName
    val redisHostsConfig = event.getServletContext.getInitParameter("redisConfigName")
    if (redisHostsConfig != null && !redisHostsConfig.isEmpty) {
      configName = redisHostsConfig
    }
    val properties = new Properties()
    val inputStream = this.getClass.getClassLoader.getResourceAsStream(configName)
    properties.load(inputStream)
    Init(properties)
    redisPoolConfigInit(event)
  }


  def Init(properties: Properties): String = {
    redisConfigInit(properties)
    ShardingRedis.init(properties)
  }

  private def getHostsSort(properties: Properties): Boolean = {
    try {
      val redisHostsSort = properties.get(redisHostsSortKey)
      if (redisHostsSort == null) {
        return true
      }
      redisHostsSort.toString.toBoolean
    } catch {
      case e: Exception => true
    }

  }

  private def redisPoolConfigInit(event: ServletContextEvent) {
    val redisPoolConfig = event.getServletContext.getInitParameter("redisPoolConfig")
    if (redisPoolConfig != null && !redisPoolConfig.isEmpty) {
      val redisPoolConfigs = redisPoolConfig.split(",")
      if (redisPoolConfigs.size != 6) {
        throw new IllegalArgumentException("redisPoolConfig[" + redisPoolConfig + "] is illegal. Tt's shoud be:minIdle,maxIdle,maxActive,maxWait,timeout")
      }
      val minIdle = redisPoolConfigs(0).toInt
      val maxIdle = redisPoolConfigs(1).toInt
      val maxActive = redisPoolConfigs(2).toInt
      val maxWait = redisPoolConfigs(3).toLong
      val testOnBorrow = redisPoolConfigs(4).toBoolean
      val timeout = redisPoolConfigs(5).toInt
      JedisPoolConfig.init(minIdle, maxIdle, maxActive, maxWait, testOnBorrow, timeout)
      ShardedJedisClientPool.rePool()
    }
  }

  private def redisConfigInit(properties: Properties) {
    try {
      val redisHostsSize = properties.get(redisHostsSizeKey)
      if (redisHostsSize != null) {
        val v = redisHostsSize.toString.trim
        if (!v.isEmpty) {
          Hosts.initHostsSize(v.toInt)
        }
      }
      Hosts.initHostsSort(getHostsSort(properties))
    } catch {
      case e: Exception => println(e.getMessage, e)
    }
  }
}
