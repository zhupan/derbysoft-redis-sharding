package com.derbysoft.redis.clients.hashsharding.core

import com.derbysoft.redis.util.RedisInfoUtil
import scala.Predef._
import com.derbysoft.redis.clients.RedisCommands
import scala.{Double, Long}
import redis.clients.jedis._
import java.lang.String

class HashShardingRedis extends RedisCommands {

  override def getResource: JedisCommands = {
    ShardedJedisClientPool.getResource
  }

  override def returnResource(jedisCommands: JedisCommands) {
    ShardedJedisClientPool.returnResource(jedisCommands.asInstanceOf[ShardedJedis])
  }

  override def get(key: String): String = {
    ShardedJedisClientPool.withClient {
      client => return client.get(key)
    }
  }

  override def set(key: String, value: String): String = {
    ShardedJedisClientPool.withClient {
      client => return client.set(key, value)
    }
  }

  override def delete(key: String) {
    ShardedJedisClientPool.withClient {
      client => {
        client.expire(key, 0)
      }
    }
  }

  override def set(key: String, value: String, unixTime: Long) {
    ShardedJedisClientPool.withClient {
      client => {
        client.set(key, value)
        client.expireAt(key, unixTime)
      }
    }
  }

  override def hget(key: String, field: String): String = {
    ShardedJedisClientPool.withClient {
      client => return client.hget(key, field)
    }
  }

  override def hset(key: String, field: String, value: String): java.lang.Long = {
    ShardedJedisClientPool.withClient {
      client => return client.hset(key, field, value)
    }
  }

  override def hmset(key: String, map: java.util.Map[String, String]): String = {
    ShardedJedisClientPool.withClient {
      client => return client.hmset(key, map)
    }
  }

  override def hmset(key: String, map: java.util.Map[String, String], unixTime: Long) {
    ShardedJedisClientPool.withClient {
      client => {
        client.hmset(key, map)
        client.expireAt(key, unixTime)
      }
    }
  }

  override def hset(key: String, field: String, value: String, unixTime: Long) {
    ShardedJedisClientPool.withClient {
      client => {
        client.hset(key, field, value)
        client.expireAt(key, unixTime)
      }
    }
  }

  override def hgetAll(key: String): java.util.Map[String, String] = {
    ShardedJedisClientPool.withClient {
      client => return client.hgetAll(key)
    }
  }

  override def hdel(key: String, fileds: String*): java.lang.Long = {
    ShardedJedisClientPool.withClient {
      client => return client.hdel(key, fileds: _*)
    }
  }

  override def expireAt(key: String, unixTime: Long): java.lang.Long = {
    ShardedJedisClientPool.withClient {
      client => return client.expireAt(key, unixTime)
    }
  }

  override def expire(key: String, expiry: Int): java.lang.Long = {
    ShardedJedisClientPool.withClient {
      client => return client.expire(key, expiry)
    }
  }

  def del(key: String): Long = {
    ShardedJedisClientPool.withClient {
      client => return client.del(key)
    }
  }

  override def exists(key: String): java.lang.Boolean = {
    ShardedJedisClientPool.withClient {
      client => return client.exists(key)
    }
  }

  override def ttl(key: String): java.lang.Long = {
    ShardedJedisClientPool.withClient {
      client => return client.ttl(key)
    }
  }

  override def keys(pattern: String): java.util.Set[String] = {
    ShardedJedisClientPool.withClient {
      client => {
        val keys = new java.util.HashSet[String]
        val iterator = client.getAllShards.iterator()
        while (iterator.hasNext) {
          val jedis = iterator.next()
          keys.addAll(jedis.keys(pattern))
        }
        return keys
      }
    }
  }

  override def flushAll(): String = {
    ShardedJedisClientPool.withClient {
      client => {
        val iterator = client.getAllShards.iterator()
        while (iterator.hasNext) {
          val jedis = iterator.next()
          jedis.flushAll()
        }
      }
    }
    "success"
  }

  private def infos(): java.util.List[String] = {
    val infos = new java.util.ArrayList[String]
    ShardedJedisClientPool.withClient {
      client => {
        val iterator = client.getAllShards.iterator()
        while (iterator.hasNext) {
          infos.add(iterator.next().info())
        }
      }
    }
    infos
  }

  override def usedMemory(): Long = {
    var totalMemory = 0L
    val iterator = infos().iterator()
    while (iterator.hasNext) {
      totalMemory += RedisInfoUtil.getUsedMemory(iterator.next())
    }
    totalMemory
  }

  override def zadd(key: String, score: Double, member: String): java.lang.Long = {
    ShardedJedisClientPool.withClient {
      client => return client.zadd(key, score, member)
    }
  }

  override def zadd(key: String, scoreMembers: java.util.Map[java.lang.Double, String]): java.lang.Long = {
    ShardedJedisClientPool.withClient {
      client => return client.zadd(key, scoreMembers)
    }
  }

  override def pipelined(shardedJedisPipeline: ShardedJedisPipeline): java.util.List[Object] = {
    ShardedJedisClientPool.withClient {
      client => {
        shardedJedisPipeline.setShardedJedis(client)
        shardedJedisPipeline.execute()
        return shardedJedisPipeline.getResults
      }
    }
  }

  override def zrangeByScore(key: String, min: Double, max: Double): java.util.Set[String] = {
    ShardedJedisClientPool.withClient {
      client => return client.zrangeByScore(key, min, max)
    }
  }

  override def zrangeByScore(key: String, min: String, max: String): java.util.Set[String] = {
    ShardedJedisClientPool.withClient {
      client => return client.zrangeByScore(key, min, max)
    }
  }

  override def zrangeByScore(key: String, min: Double, max: Double, offset: Int, count: Int): java.util.Set[String] = {
    ShardedJedisClientPool.withClient {
      client => return client.zrangeByScore(key, min, max, offset, count)
    }
  }

  override def zrangeByScore(key: String, min: String, max: String, offset: Int, count: Int): java.util.Set[String] = {
    ShardedJedisClientPool.withClient {
      client => return client.zrangeByScore(key, min, max, offset, count)
    }
  }

  override def zrem(key: String, member: String*): java.lang.Long = {
    ShardedJedisClientPool.withClient {
      client => return client.zrem(key, member: _*)
    }
  }

  override def zcount(key: String, min: Double, max: Double): java.lang.Long = {
    ShardedJedisClientPool.withClient {
      client => return client.zcount(key, min, max)
    }
  }

  override def zcount(key: String, min: String, max: String): java.lang.Long = {
    ShardedJedisClientPool.withClient {
      client => return client.zcount(key, min, max)
    }
  }

  override def setbit(key: String, offset: Long, value: Boolean): java.lang.Boolean = {
    ShardedJedisClientPool.withClient {
      client => return client.setbit(key, offset, value)
    }
  }

  override def getbit(key: String, offset: Long): java.lang.Boolean = {
    ShardedJedisClientPool.withClient {
      client => return client.getbit(key, offset)
    }
  }

  override def setrange(key: String, offset: Long, value: String): java.lang.Long = {
    ShardedJedisClientPool.withClient {
      client => return client.setrange(key, offset, value)
    }
  }

  override def getrange(key: String, startOffset: Long, endOffset: Long): String = {
    ShardedJedisClientPool.withClient {
      client => return client.getrange(key, startOffset, endOffset)
    }
  }

  override def getSet(key: String, value: String): String = {
    ShardedJedisClientPool.withClient {
      client => return client.getSet(key, value)
    }
  }

  override def setnx(key: String, value: String): java.lang.Long = {
    ShardedJedisClientPool.withClient {
      client => return client.setnx(key, value)
    }
  }

  override def setex(key: String, seconds: Int, value: String): String = {
    ShardedJedisClientPool.withClient {
      client => return client.setex(key, seconds, value)
    }
  }

  override def decrBy(key: String, long: Long): java.lang.Long = {
    ShardedJedisClientPool.withClient {
      client => return client.decrBy(key, long)
    }
  }

  override def decr(key: String): java.lang.Long = {
    ShardedJedisClientPool.withClient {
      client => return client.decr(key)
    }
  }

  override def incrBy(key: String, long: Long): java.lang.Long = {
    ShardedJedisClientPool.withClient {
      client => return client.incrBy(key, long)
    }
  }

  override def incr(key: String): java.lang.Long = {
    ShardedJedisClientPool.withClient {
      client => return client.incr(key)
    }
  }

  override def append(key: String, value: String): java.lang.Long = {
    ShardedJedisClientPool.withClient {
      client => return client.append(key, value)
    }
  }

  override def substr(key: String, start: Int, end: Int): String = {
    ShardedJedisClientPool.withClient {
      client => return client.substr(key, start, end)
    }
  }

  override def hsetnx(key: String, field: String, value: String): java.lang.Long = {
    ShardedJedisClientPool.withClient {
      client => return client.hsetnx(key, field, value)
    }
  }

  override def hincrBy(key: String, field: String, value: Long): java.lang.Long = {
    ShardedJedisClientPool.withClient {
      client => return client.hincrBy(key, field, value)
    }
  }

  override def hexists(key: String, field: String): java.lang.Boolean = {
    ShardedJedisClientPool.withClient {
      client => return client.hexists(key, field)
    }
  }

  override def hlen(key: String): java.lang.Long = {
    ShardedJedisClientPool.withClient {
      client => return client.hlen(key)
    }
  }

  override def hkeys(key: String): java.util.Set[String] = {
    ShardedJedisClientPool.withClient {
      client => return client.hkeys(key)
    }
  }

  override def hvals(key: String): java.util.List[String] = {
    ShardedJedisClientPool.withClient {
      client => return client.hvals(key)
    }
  }

  override def rpush(key: String, string: String*): java.lang.Long = {
    ShardedJedisClientPool.withClient {
      client => return client.rpush(key, string: _*)
    }
  }

  override def lpush(key: String, string: String*): java.lang.Long = {
    ShardedJedisClientPool.withClient {
      client => return client.lpush(key, string: _*)
    }
  }

  override def llen(key: String): java.lang.Long = {
    ShardedJedisClientPool.withClient {
      client => return client.llen(key)
    }
  }

  override def lrange(key: String, start: Long, end: Long): java.util.List[String] = {
    ShardedJedisClientPool.withClient {
      client => return client.lrange(key, start, end)
    }
  }

  override def ltrim(key: String, start: Long, end: Long): String = {
    ShardedJedisClientPool.withClient {
      client => return client.ltrim(key, start, end)
    }
  }

  override def lindex(key: String, index: Long): String = {
    ShardedJedisClientPool.withClient {
      client => return client.lindex(key, index)
    }
  }

  override def lset(key: String, index: Long, value: String): String = {
    ShardedJedisClientPool.withClient {
      client => return client.lset(key, index, value)
    }
  }

  override def lrem(key: String, count: Long, value: String): java.lang.Long = {
    ShardedJedisClientPool.withClient {
      client => return client.lrem(key, count, value)
    }
  }

  override def lpop(key: String): java.lang.String = {
    ShardedJedisClientPool.withClient {
      client => return client.lpop(key)
    }
  }

  override def rpop(key: String): java.lang.String = {
    ShardedJedisClientPool.withClient {
      client => return client.rpop(key)
    }
  }

  override def sadd(key: String, member: String*): java.lang.Long = {
    ShardedJedisClientPool.withClient {
      client => return client.sadd(key, member: _*)
    }
  }

  override def smembers(key: String): java.util.Set[String] = {
    ShardedJedisClientPool.withClient {
      client => return client.smembers(key)
    }
  }

  override def srem(key: String, member: String*): java.lang.Long = {
    ShardedJedisClientPool.withClient {
      client => return client.srem(key, member: _*)
    }
  }

  override def spop(key: String): java.lang.String = {
    ShardedJedisClientPool.withClient {
      client => return client.spop(key)
    }
  }

  override def scard(key: String): java.lang.Long = {
    ShardedJedisClientPool.withClient {
      client => return client.scard(key)
    }
  }

  override def sismember(key: String, member: String): java.lang.Boolean = {
    ShardedJedisClientPool.withClient {
      client => return client.sismember(key, member)
    }
  }

  override def srandmember(key: String): java.lang.String = {
    ShardedJedisClientPool.withClient {
      client => return client.srandmember(key)
    }
  }

  override def zrange(key: String, start: Long, end: Long): java.util.Set[String] = {
    ShardedJedisClientPool.withClient {
      client => return client.zrange(key, start, end)
    }
  }

  override def zincrby(key: String, score: Double, member: String): java.lang.Double = {
    ShardedJedisClientPool.withClient {
      client => return client.zincrby(key, score, member)
    }
  }

  override def zrank(key: String, member: String): java.lang.Long = {
    ShardedJedisClientPool.withClient {
      client => return client.zrank(key, member)
    }
  }

  override def zrevrank(key: String, member: String): java.lang.Long = {
    ShardedJedisClientPool.withClient {
      client => return client.zrevrank(key, member)
    }
  }

  override def zrevrange(key: String, start: Long, end: Long): java.util.Set[String] = {
    ShardedJedisClientPool.withClient {
      client => return client.zrevrange(key, start, end)
    }
  }

  override def zrangeWithScores(key: String, start: Long, end: Long): java.util.Set[Tuple] = {
    ShardedJedisClientPool.withClient {
      client => return client.zrangeWithScores(key, start, end)
    }
  }

  override def zrevrangeWithScores(key: String, start: Long, end: Long): java.util.Set[Tuple] = {
    ShardedJedisClientPool.withClient {
      client => return client.zrevrangeWithScores(key, start, end)
    }
  }

  override def zcard(key: String): java.lang.Long = {
    ShardedJedisClientPool.withClient {
      client => return client.zcard(key)
    }
  }

  override def zscore(key: String, member: String): java.lang.Double = {
    ShardedJedisClientPool.withClient {
      client => return client.zscore(key, member)
    }
  }

  override def sort(key: String): java.util.List[String] = {
    ShardedJedisClientPool.withClient {
      client => return client.sort(key)
    }
  }

  override def sort(key: String, sortingParameters: SortingParams): java.util.List[String] = {
    ShardedJedisClientPool.withClient {
      client => return client.sort(key, sortingParameters)
    }
  }

  override def zrevrangeByScore(key: String, min: Double, max: Double): java.util.Set[String] = {
    ShardedJedisClientPool.withClient {
      client => return client.zrevrangeByScore(key, min, max)
    }
  }

  override def zrevrangeByScore(key: String, min: String, max: String): java.util.Set[String] = {
    ShardedJedisClientPool.withClient {
      client => return client.zrevrangeByScore(key, min, max)
    }
  }

  override def zrevrangeByScore(key: String, min: Double, max: Double, offset: Int, count: Int): java.util.Set[String] = {
    ShardedJedisClientPool.withClient {
      client => return client.zrevrangeByScore(key, min, max)
    }
  }

  override def zrevrangeByScore(key: String, min: String, max: String, offset: Int, count: Int): java.util.Set[String] = {
    ShardedJedisClientPool.withClient {
      client => return client.zrevrangeByScore(key, min, max)
    }
  }

  override def zrangeByScoreWithScores(key: String, min: Double, max: Double): java.util.Set[Tuple] = {
    ShardedJedisClientPool.withClient {
      client => return client.zrangeByScoreWithScores(key, min, max)
    }
  }

  override def zrangeByScoreWithScores(key: String, min: String, max: String): java.util.Set[Tuple] = {
    ShardedJedisClientPool.withClient {
      client => return client.zrangeByScoreWithScores(key, min, max)
    }
  }

  override def zrevrangeByScoreWithScores(key: String, min: Double, max: Double): java.util.Set[Tuple] = {
    ShardedJedisClientPool.withClient {
      client => return client.zrevrangeByScoreWithScores(key, min, max)
    }
  }

  override def zrevrangeByScoreWithScores(key: String, min: String, max: String): java.util.Set[Tuple] = {
    ShardedJedisClientPool.withClient {
      client => return client.zrevrangeByScoreWithScores(key, min, max)
    }
  }

  override def zrangeByScoreWithScores(key: String, min: Double, max: Double, offset: Int, count: Int): java.util.Set[Tuple] = {
    ShardedJedisClientPool.withClient {
      client => return client.zrangeByScoreWithScores(key, min, max, offset, count)
    }
  }

  override def zrangeByScoreWithScores(key: String, min: String, max: String, offset: Int, count: Int): java.util.Set[Tuple] = {
    ShardedJedisClientPool.withClient {
      client => return client.zrangeByScoreWithScores(key, min, max, offset, count)
    }
  }

  override def zrevrangeByScoreWithScores(key: String, min: Double, max: Double, offset: Int, count: Int): java.util.Set[Tuple] = {
    ShardedJedisClientPool.withClient {
      client => return client.zrevrangeByScoreWithScores(key, min, max, offset, count)
    }
  }

  override def zrevrangeByScoreWithScores(key: String, min: String, max: String, offset: Int, count: Int): java.util.Set[Tuple] = {
    ShardedJedisClientPool.withClient {
      client => return client.zrevrangeByScoreWithScores(key, min, max, offset, count)
    }
  }

  override def zremrangeByRank(key: String, start: Long, end: Long): java.lang.Long = {
    ShardedJedisClientPool.withClient {
      client => return client.zremrangeByRank(key, start, end)
    }
  }

  override def zremrangeByScore(key: String, start: Double, end: Double): java.lang.Long = {
    ShardedJedisClientPool.withClient {
      client => return client.zremrangeByScore(key, start, end)
    }
  }

  override def linsert(key: String, where: BinaryClient.LIST_POSITION, pivot: String, value: String): java.lang.Long = {
    ShardedJedisClientPool.withClient {
      client => return client.linsert(key, where, pivot, value)
    }
  }

  override def hmget(key: String, fileds: String*): java.util.List[String] = {
    ShardedJedisClientPool.withClient {
      client => return client.hmget(key, fileds: _*)
    }
  }

  override def `type`(key: String): String = {
    ShardedJedisClientPool.withClient {
      client => return client.`type`(key)
    }
  }

  override def lpushx(key: String, string: String): java.lang.Long = {
    ShardedJedisClientPool.withClient {
      client => return client.lpushx(key, string)
    }
  }

  override def rpushx(key: String, string: String): java.lang.Long = {
    ShardedJedisClientPool.withClient {
      client => return client.rpushx(key, string)
    }
  }

  override def zremrangeByScore(key: String, start: String, end: String): java.lang.Long = {
    ShardedJedisClientPool.withClient {
      client => return client.zremrangeByScore(key, start, end)
    }
  }
}