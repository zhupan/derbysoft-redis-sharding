package com.derbysoft.redis.clients

import redis.clients.jedis.{JedisCommands, ShardedJedisPipeline}


trait RedisCommands extends JedisCommands {

  def getResource: JedisCommands

  def returnResource(jedisCommands: JedisCommands)

  def returnBrokenResource(jedisCommands: JedisCommands)

  def delete(key: String)

  def set(key: String, value: String, unixTime: Long)

  def hget(key: String, field: String): String

  def hset(key: String, field: String, value: String, unixTime: Long)

  def hmset(key: String, map: java.util.Map[String, String], unixTime: Long)

  def keys(pattern: String): java.util.Set[String]

  def flushAll(): String

  def usedMemory(): Long

  def pipelined(shardedJedisPipeline: ShardedJedisPipeline): java.util.List[Object]

}
