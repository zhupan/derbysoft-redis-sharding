package com.derbysoft.redis.clients.common.config

trait HostRule {

  def getHost(key: String): String

}
