package com.derbysoft.redis.clients.common.listener

import com.derbysoft.redis.clients.ShardingRedis
import java.util.ResourceBundle
import javax.servlet.ServletContextEvent
import com.derbysoft.redis.clients.normal.JedisPoolConfig
import com.derbysoft.redis.clients.common.config.HostKey
import com.derbysoft.redis.clients.hashsharding.core.ShardedJedisClientPool
import org.apache.commons.logging.LogFactory

protected object RedisInit {

  private val redisHostsSize = "redis.hosts.size"

  private val redisHostsFile = "redis.hosts.file"

  private val logger = LogFactory.getLog(this.getClass)

  def apply(event: ServletContextEvent) {
    var environment = "environment"
    val redisHostsConfig = event.getServletContext.getInitParameter("redisHostsConfig")
    if (redisHostsConfig != null && !redisHostsConfig.isEmpty) {
      environment = redisHostsConfig.replaceAll(".properties", "")
    }
    val environmentProperties = ResourceBundle.getBundle(environment)
    redisHostsSizeInit(environmentProperties)
    val redisHostsFile = environmentProperties.getString(RedisInit.redisHostsFile)
    if (redisHostsFile != null) {
      ShardingRedis.init(redisHostsFile)
    }
    redisPoolConfigInit(event)
  }


  def redisPoolConfigInit(event: ServletContextEvent) {
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

  def redisHostsSizeInit(environment: ResourceBundle) {
    try {
      val redisHostsSize = environment.getString(RedisInit.redisHostsSize)
      if (redisHostsSize != null && !redisHostsSize.isEmpty) {
        HostKey.init(Integer.valueOf(redisHostsSize.trim))
      }
    } catch {
      case e: Exception => {
        logger.info(e.getMessage, e)
      }
    }
  }
}
