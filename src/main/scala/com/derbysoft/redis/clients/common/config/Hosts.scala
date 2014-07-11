package com.derbysoft.redis.clients.common.config

object Hosts {

  var redisHostsSize = 32

  var redisHostsSort = false

  val hostPrefix = "redis.host."

  def initHostsSize(redisHostsSize: Int) {
    this.redisHostsSize = redisHostsSize
  }

  def initHostsSort(redisHostsSort: Boolean) {
    this.redisHostsSort = redisHostsSort
  }

}
