package com.derbysoft.redis.clients.common.listener

import com.derbysoft.redis.clients.ShardingRedis
import java.util.ResourceBundle
import javax.servlet.ServletContextEvent
import com.derbysoft.redis.clients.normal.JedisPoolConfig
import com.derbysoft.redis.clients.common.config.Hosts
import com.derbysoft.redis.clients.hashsharding.core.ShardedJedisClientPool
import org.apache.commons.logging.LogFactory

object RedisInit {

  private val redisHostsSizeKey = "redis.hosts.size"

  private val redisHostsFileKey = "redis.hosts.file"

  private val redisHostsSortKey = "redis.hosts.sort"

  private val logger = LogFactory.getLog(this.getClass)

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
    val redisHostsSort = environment.getString(redisHostsSortKey)
    if (redisHostsSort == null) {
      return false
    }
    redisHostsSort.toBoolean
  }

  private def redisPoolConfigInit(event: ServletContextEvent) {
    val redisPoolConfig = event.getServletContext.getInitParameter("redisPoolConfig")
    if (redisPoolConfig != null && !redisPoolConfig.isEmpty) {
      val redisPoolConfigs = redisPoolConfig.split(",")
      if (redisPoolConfigs.size != 6) {
        throw new IllegalArgumentException("redisPoolConfig[" + redisPoolConfig + "] is illegal. Tt's shoud be:minIdle,maxIdle,maxActive,maxWait,timeout")
      }
      val minIdle = Integer.valueOf(redisPoolConfigs(0))
      val maxIdle = Integer.valueOf(redisPoolConfigs(1))
      val maxActive = Integer.valueOf(redisPoolConfigs(2))
      val maxWait = java.lang.Long.valueOf(redisPoolConfigs(3))
      val testOnBorrow = java.lang.Boolean.valueOf(redisPoolConfigs(4))
      val timeout = Integer.valueOf(redisPoolConfigs(5))
      JedisPoolConfig.init(minIdle, maxIdle, maxActive, maxWait, testOnBorrow, timeout)
      ShardedJedisClientPool.rePool()
    }
  }

  private def redisHostsInit(environment: ResourceBundle) {
    try {
      val redisHostsSize = environment.getString(redisHostsSizeKey)
      if (redisHostsSize != null && !redisHostsSize.isEmpty) {
        Hosts.initHostsSize(Integer.valueOf(redisHostsSize.trim))
      }
      Hosts.initHostsSort(getHostsSort(environment))
    } catch {
      case e: Exception => logger.info(e.getMessage, e)
    }
  }
}
