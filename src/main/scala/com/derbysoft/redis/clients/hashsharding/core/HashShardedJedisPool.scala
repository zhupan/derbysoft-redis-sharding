package com.derbysoft.redis.clients.hashsharding.core

import redis.clients.jedis.JedisShardInfo
import com.derbysoft.redis.clients.ShardingRedis
import com.derbysoft.redis.clients.normal.JedisPoolConfigInstance
import com.derbysoft.redis.clients.common.config.HostKey

object HashShardedJedisPool {

  private val shards = new java.util.ArrayList[JedisShardInfo]

  def apply(): ShardedJedisPool = {
    new ShardedJedisPool(JedisPoolConfigInstance(), shards)
  }

  def allShards: java.util.List[JedisShardInfo] = {
    shards.clone().asInstanceOf[java.util.List[JedisShardInfo]]
  }

  def initShards() {
    shards.clear()
    println("shards size:" + ShardingRedis.hostsMap.size)
    ShardingRedis.hostsMap.foreach((h: (String, String)) => {
      shards.add(JedisShardInfoInstance(h._2))
      println(h._1)
    })
    if (shards.size() != HostKey.redisHostsSize) {
      throw new IllegalArgumentException("Redis hosts size[" + shards.size + "] is illegal.")
    }
  }
}
