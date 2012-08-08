package com.derbysoft.redis.clients.common.listener

import javax.servlet.{ServletContextEvent, ServletContextListener}

class RedisHostsInitListener extends ServletContextListener {

  override def contextInitialized(event: ServletContextEvent) {
    RedisInit(event)
  }

  override def contextDestroyed(event: ServletContextEvent) {

  }

}
