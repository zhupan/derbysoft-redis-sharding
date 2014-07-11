package com.derbysoft.redis.clients.hashsharding.core

import redis.clients.jedis.JedisShardInfo
import com.derbysoft.redis.clients.ShardingRedis
import com.derbysoft.redis.clients.normal.JedisPoolConfigInstance
import com.derbysoft.redis.clients.common.config.Hosts
import scala.collection.JavaConversions._
import java.util

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
    val hosts = getHosts()
    println("shards size:" + hosts.size)
    hosts.foreach(h => shards.add(JedisShardInfoInstance(h)))
    if (shards.size() != Hosts.redisHostsSize) {
      throw new IllegalArgumentException("Redis hosts size[" + shards.size + "] is illegal.")
    }
  }

  def getHosts(): util.Collection[String] = {
    val hosts = getCollection
    val keys = getCollection
    ShardingRedis.hostsMap.foreach((h: (String, String)) => {
      hosts.add(h._2)
      keys.add(h._1)
    })
    keys.foreach(k => println(k))
    hosts
  }

  def getCollection: util.Collection[String] = {
    if (Hosts.redisHostsSort) {
      return new util.TreeSet()
    }
    new util.ArrayList()
  }

}
