package com.derbysoft.redis.clients.common.config

object HostKey {

  var redisHostsSize = 32

  val hostPrefix = "redis.host."

  def init(redisHostsSize: Int) {
    this.redisHostsSize = redisHostsSize
  }

}
