package com.derbysoft.redis.clients.datesharding.core

import java.util.concurrent.ConcurrentHashMap
import actors.Actor._
import com.derbysoft.redis.util.RedisInfoUtil
import java.util.{Iterator, HashSet}
import com.derbysoft.redis.clients.{ShardingRedis, RedisCommands}
import scala.Predef._
import scala.Double
import redis.clients.jedis.ShardedJedisPipeline

class DateShardingJedis {//extends RedisCommands {

//  override def get(key: String): String = {
//    val pool = JedisClientPoolFactory(GetHost(key))
//    pool.withClient {
//      client => return client.get(key)
//    }
//  }
//
//  override def set(key: String, value: String): Boolean = {
//    val pool = JedisClientPoolFactory(GetHost(key))
//    pool.withClient {
//      client => client.set(key, value)
//    }
//    return true
//  }
//
//  override def set(key: String, value: String, unixTime: Long): Boolean = {
//    val pool = JedisClientPoolFactory(GetHost(key))
//    pool.withClient {
//      client => {
//        client.set(key, value)
//        client.expireAt(key, unixTime)
//      }
//    }
//    return true
//  }
//
//  override def hget(key: String, field: String): String = {
//    val pool = JedisClientPoolFactory(GetHost(key))
//    pool.withClient {
//      client => return client.hget(key, field)
//    }
//  }
//
//  override def hset(key: String, field: String, value: String): Boolean = {
//    val pool = JedisClientPoolFactory(GetHost(key))
//    pool.withClient {
//      client => client.hset(key, field, value)
//    }
//    return true
//  }
//
//  def hset(key: String, map: java.util.Map[String, String]): Boolean = {
//    val pool = JedisClientPoolFactory(GetHost(key))
//    pool.withClient {
//      client => {
//        val iterator: Iterator[String] = map.keySet().iterator()
//        while (iterator.hasNext) {
//          val field = iterator.next()
//          client.hset(key, field, map.get(field))
//        }
//      }
//    }
//    return true
//  }
//
//  def hset(key: String, map: java.util.Map[String, String], unixTime: Long): Boolean = {
//    val pool = JedisClientPoolFactory(GetHost(key))
//    pool.withClient {
//      client => {
//        val iterator: Iterator[String] = map.keySet().iterator()
//        while (iterator.hasNext) {
//          val field = iterator.next()
//          client.hset(key, field, map.get(field))
//        }
//        client.expireAt(key, unixTime)
//      }
//    }
//    return true
//  }
//
//
//  override def hset(key: String, field: String, value: String, unixTime: Long): Boolean = {
//    val pool = JedisClientPoolFactory(GetHost(key))
//    pool.withClient {
//      client => {
//        client.hset(key, field, value)
//        client.expireAt(key, unixTime)
//      }
//    }
//    return true
//  }
//
//  override def hgetAll(key: String): java.util.Map[String, String] = {
//    val pool = JedisClientPoolFactory(GetHost(key))
//    pool.withClient {
//      client => return client.hgetAll(key)
//    }
//  }
//
//  override def hdel(key: String, field: String): Boolean = {
//    val pool = JedisClientPoolFactory(GetHost(key))
//    pool.withClient {
//      client => client.hdel(key, field)
//    }
//    return true
//  }
//
//
//  override def expireAt(key: String, unixTime: Long): Boolean = {
//    val pool = JedisClientPoolFactory(GetHost(key))
//    pool.withClient {
//      client => client.expireAt(key, unixTime)
//    }
//    return true
//  }
//
//  override def expire(key: String, expiry: Int): Boolean = {
//    val pool = JedisClientPoolFactory(GetHost(key))
//    pool.withClient {
//      client => client.expire(key, expiry)
//    }
//    return true
//  }
//
//  override def del(key: String): Boolean = {
//    val pool = JedisClientPoolFactory(GetHost(key))
//    pool.withClient {
//      client => client.del(key)
//    }
//    return true
//  }
//
//  override def exists(key: String): Boolean = {
//    val pool = JedisClientPoolFactory(GetHost(key))
//    pool.withClient {
//      client => client.exists(key)
//    }
//  }
//
//  override def ttl(key: String): Int = {
//    val pool = JedisClientPoolFactory(GetHost(key))
//    pool.withClient {
//      client => return client.ttl(key).intValue()
//    }
//  }
//
//  override def keys(pattern: String): java.util.Set[String] = {
//    val keys = new HashSet[String]
//    val count = new ConcurrentHashMap[String, String]
//    val redisActor = actor {
//      receive {
//        case hostKey => {
//          val pool = JedisClientPoolFactory(hostKey.toString)
//          pool.withClient {
//            client => {
//              keys.addAll(client.keys(pattern))
//            }
//          }
//          count.put(hostKey.toString, "")
//        }
//      }
//    }
//    ShardingRedis.hostsMap.keySet.foreach(hostKey => {
//      redisActor ! hostKey
//    })
//    while (true) {
//      if (ShardingRedis.hostsMap.size == count.size()) {
//        return keys
//      }
//      Thread.sleep(1)
//    }
//    keys
//  }
//
//  override def flushAll(): Boolean = {
//    try {
//      ShardingRedis.hostsMap.keySet.foreach(hostKey => {
//        val pool = JedisClientPoolFactory(hostKey)
//        pool.withClient {
//          client => client.flushAll()
//        }
//      })
//    } catch {
//      case e => {
//        println(e)
//        return false
//      }
//    }
//    return true
//  }
//
//  private def infos(): java.util.Map[String, String] = {
//    val infos = new java.util.HashMap[String, String]
//    ShardingRedis.hostsMap.keySet.foreach(hostKey => {
//      val pool = JedisClientPoolFactory(hostKey)
//      pool.withClient {
//        client => infos.put(hostKey, client.info)
//      }
//    })
//    return infos
//  }
//
//  override def usedMemory(): Long = {
//    var totalMemory = 0L
//    val iterator = infos.values().iterator()
//    while (iterator.hasNext) {
//      totalMemory += RedisInfoUtil.getUsedMemory(iterator.next())
//    }
//    totalMemory
//  }
//
//  override def pipelined(shardedJedisPipeline: ShardedJedisPipeline): java.util.List[AnyRef] = {
//    return null;
//  }
//
//  override def zadd(key: String, score: Double, member: String): Boolean = {
//    val pool = JedisClientPoolFactory(GetHost(key))
//    pool.withClient {
//      client => client.zadd(key, score, member)
//    }
//    return true;
//  }
//
//  override def zrangeByScore(key: String, min: Double, max: Double): java.util.Set[String] = {
//    val pool = JedisClientPoolFactory(GetHost(key))
//    pool.withClient {
//      client => return client.zrangeByScore(key, min, max)
//    }
//  }
//
//  override def zrangeByScore(key: String, min: Double, max: Double, offset: Int, count: Int): java.util.Set[String] = {
//    val pool = JedisClientPoolFactory(GetHost(key))
//    pool.withClient {
//      client => return client.zrangeByScore(key, min, max, offset, count)
//    }
//  }
//
//  override def zrem(key: String, member: String): Boolean = {
//    val pool = JedisClientPoolFactory(GetHost(key))
//    pool.withClient {
//      client => client.zrem(key, member)
//    }
//    return true;
//  }
//
//  override def zcount(key: String, min: Double, max: Double): Long = {
//    val pool = JedisClientPoolFactory(GetHost(key))
//    pool.withClient {
//      client => return client.zcount(key, min, max)
//    }
//  }

}