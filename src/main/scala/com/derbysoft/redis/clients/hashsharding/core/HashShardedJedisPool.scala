package com.derbysoft.redis.clients.hashsharding.core

import redis.clients.jedis.{JedisShardInfo, ShardedJedisPool}
import com.derbysoft.redis.clients.ShardingRedis
import com.derbysoft.redis.clients.normal.JedisPoolConfigInstance
import com.derbysoft.redis.clients.common.config.HostKey

object HashShardedJedisPool {

  val shards = new java.util.ArrayList[JedisShardInfo]

  def apply(): ShardedJedisPool = {
    new ShardedJedisPool(JedisPoolConfigInstance(), shards)
  }

  def initShards() {
    shards.clear()
    ShardingRedis.hostsMap.foreach((h: (String, String)) => {
      shards.add(JedisShardInfoInstance(h._2))
    })
    if (shards.size() != HostKey.redisHostsSize) {
      throw new IllegalArgumentException("Redis hosts size[" + shards.size + "] is illegal.")
    }
  }
}
