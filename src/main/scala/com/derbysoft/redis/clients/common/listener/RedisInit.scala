package com.derbysoft.redis.clients.common.listener

import com.derbysoft.redis.clients.ShardingRedis
import java.util.ResourceBundle
import javax.servlet.ServletContextEvent
import com.derbysoft.redis.clients.normal.JedisPoolConfig
import com.derbysoft.redis.clients.common.config.Hosts
import com.derbysoft.redis.clients.hashsharding.core.ShardedJedisClientPool

object RedisInit {

  private val redisHostsSizeKey = "redis.hosts.size"

  private val redisHostsFileKey = "redis.hosts.file"

  private val redisHostsSortKey = "redis.hosts.sort"

  def apply(event: ServletContextEvent) {
    var environment = "environment"
    val redisHostsConfig = event.getServletContext.getInitParameter("redisHostsConfig")
    if (redisHostsConfig != null && !redisHostsConfig.isEmpty) {
      environment = redisHostsConfig.replaceAll(".properties", "")
    }
    val environmentProperties = ResourceBundle.getBundle(environment)
    redisHostsInit(environmentProperties)
    val redisHostsFile = environmentProperties.getString(redisHostsFileKey)
    if (redisHostsFile != null) {
      ShardingRedis.init(redisHostsFile)
    }
    redisPoolConfigInit(event)
  }


  private def getHostsSort(environment: ResourceBundle): Boolean = {
    try {
      val redisHostsSort = environment.getString(redisHostsSortKey)
      if (redisHostsSort == null) {
        return false
      }
      redisHostsSort.toBoolean
    } catch {
      case e: Exception => return false
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

  private def redisHostsInit(environment: ResourceBundle) {
    try {
      val redisHostsSize = environment.getString(redisHostsSizeKey)
      if (redisHostsSize != null && !redisHostsSize.isEmpty) {
        Hosts.initHostsSize(redisHostsSize.trim.toInt)
      }
      Hosts.initHostsSort(getHostsSort(environment))
    } catch {
      case e: Exception => println(e.getMessage, e)
    }
  }
}
