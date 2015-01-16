package com.derbysoft.redis.clients.common.listener

import com.derbysoft.redis.clients.ShardingRedis
import java.util.ResourceBundle
import javax.servlet.ServletContextEvent
import com.derbysoft.redis.clients.normal.JedisPoolConfig
import com.derbysoft.redis.clients.common.config.Hosts
import com.derbysoft.redis.clients.hashsharding.core.ShardedJedisClientPool

object RedisInit {

  val redisConfigName = "redis-sharding-config"

  val redisHostsSizeKey = "redis.hosts.size"

  val redisHostsSortKey = "redis.hosts.sort"

  def apply(event: ServletContextEvent) {
    var configName = redisConfigName
    val redisHostsConfig = event.getServletContext.getInitParameter("redisConfigName")
    if (redisHostsConfig != null && !redisHostsConfig.isEmpty) {
      configName = redisHostsConfig.replaceAll(".properties", "")
    }
    val configProperties = ResourceBundle.getBundle(configName)
    redisConfigInit(configProperties)
    ShardingRedis.init(configProperties)
    redisPoolConfigInit(event)
  }


  private def getHostsSort(config: ResourceBundle): Boolean = {
    try {
      val redisHostsSort = config.getString(redisHostsSortKey)
      if (redisHostsSort == null) {
        return true
      }
      redisHostsSort.toBoolean
    } catch {
      case e: Exception => return true
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

  private def redisConfigInit(config: ResourceBundle) {
    try {
      val redisHostsSize = config.getString(redisHostsSizeKey)
      if (redisHostsSize != null && !redisHostsSize.isEmpty) {
        Hosts.initHostsSize(redisHostsSize.trim.toInt)
      }
      Hosts.initHostsSort(getHostsSort(config))
    } catch {
      case e: Exception => println(e.getMessage, e)
    }
  }
}
