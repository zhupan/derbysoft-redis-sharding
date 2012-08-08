//package com.derbysoft.redis.clients.datesharding.core
//
//import com.derbysoft.redis.clients.RedisCommands
//import java.util.HashSet
//import java.util.concurrent.ConcurrentHashMap
//import actors.Actor._
//
class DateShardingSedis {
//
//  override def get(key: String): String = {
//    val pool = SedisClientPoolFactory.get(GetHost(key))
//    pool.withClient {
//      client => {
//        client.get[String](key) match {
//          case Some(value) => return value
//          case None => return null
//        }
//      }
//    }
//  }
//
//  override def set(key: String, value: String): Boolean = {
//    val pool = SedisClientPoolFactory.get(GetHost(key))
//    pool.withClient {
//      client => {
//        client.set(key, value)
//      }
//    }
//  }
//
//  override def set(key: String, value: String, unixTime: Long): Boolean = {
//    val pool = SedisClientPoolFactory.get(GetHost(key))
//    pool.withClient {
//      client => {
//        client.set(key, value)
//        client.expireAt(key, unixTime)
//      }
//    }
//  }
//
//  override def hget(key: String, field: String): String = {
//    val pool = SedisClientPoolFactory.get(GetHost(key))
//    pool.withClient {
//      client => {
//        client.hget(key, field) match {
//          case Some(value) => return value
//          case None => return null
//        }
//      }
//    }
//  }
//
//  override def hset(key: String, field: String, value: String): Boolean = {
//    val pool = SedisClientPoolFactory.get(GetHost(key))
//    pool.withClient {
//      client => {
//        client.hset(key, field, value)
//      }
//    }
//  }
//
//  override def hset(key: String, field: String, value: String, unixTime: Long): Boolean = {
//    val pool = SedisClientPoolFactory.get(GetHost(key))
//    pool.withClient {
//      client => {
//        client.hset(key, field, value)
//        client.expireAt(key, unixTime)
//      }
//    }
//  }
//
//  override def hgetAll(key: String): java.util.Map[String, String] = {
//    val pool = SedisClientPoolFactory.get(GetHost(key))
//    pool.withClient {
//      client => {
//        client.hgetall(key) match {
//          case Some(map) => toJavaMap(map)
//          case None => return new java.util.HashMap[java.lang.String, java.lang.String]
//        }
//      }
//    }
//  }
//
//  override def hdel(key: String, field: String): Boolean = {
//    val pool = SedisClientPoolFactory.get(GetHost(key))
//    pool.withClient {
//      client => {
//        client.hdel(key, field)
//      }
//    }
//    return true
//  }
//
//
//  override def expireAt(key: String, unixTime: Long): Boolean = {
//    val pool = SedisClientPoolFactory.get(GetHost(key))
//    pool.withClient {
//      client => {
//        client.expireAt(key, unixTime)
//      }
//    }
//  }
//
//  override def expire(key: String, expiry: Int): Boolean = {
//    val pool = SedisClientPoolFactory.get(GetHost(key))
//    pool.withClient {
//      client => {
//        client.expire(key, expiry.intValue())
//      }
//    }
//  }
//
//  override def del(key: String): Boolean = {
//    val pool = SedisClientPoolFactory.get(GetHost(key))
//    pool.withClient {
//      client => {
//        client.del(key)
//      }
//    }
//    return true
//  }
//
//  override def exists(key: String): Boolean = {
//    val pool = SedisClientPoolFactory.get(GetHost(key))
//    pool.withClient {
//      client => {
//        client.exists(key)
//      }
//    }
//  }
//
//  override def ttl(key: String): Int = {
//    val pool = SedisClientPoolFactory.get(GetHost(key))
//    pool.withClient {
//      client => {
//        client.ttl(key) match {
//          case Some(value) => return value
//          case None => return 0
//        }
//      }
//    }
//  }
//
//  override def keys(pattern: String): java.util.Set[String] = {
//    val keys = new HashSet[String]
//    val count = new ConcurrentHashMap[String, String]
//    val redisActor = actor {
//      receive {
//        case poolKey => {
//          val pool = SedisClientPoolFactory.all.get(poolKey)
//          pool.withClient {
//            client => {
//              client.keys(pattern) match {
//                case Some(list) => list.foreach(value => {
//                  value match {
//                    case Some(key) => keys.add(key)
//                    case None =>
//                  }
//                })
//                case None =>
//              }
//            }
//          }
//          count.put(poolKey.toString, "")
//        }
//      }
//    }
//    val all = SedisClientPoolFactory.all.keys()
//    while (all.hasMoreElements) {
//      redisActor ! all.nextElement()
//    }
//    while (true) {
//      if (count.size() == SedisClientPoolFactory.all.size()) {
//        return keys
//      }
//      Thread.sleep(1)
//    }
//    keys
//  }
//
//  override def flushAll(): Boolean = {
//    val all = SedisClientPoolFactory.all.keys()
//    try {
//      while (all.hasMoreElements) {
//        val poolKey = all.nextElement()
//        val pool = SedisClientPoolFactory.all.get(poolKey)
//        pool.withClient {
//          client => {
//            client.flushall
//          }
//        }
//      }
//    } catch {
//      case e => {
//        println(e)
//        return false
//      }
//    }
//    return true
//  }
//
//  private def toJavaMap(map: Map[String, String]): java.util.Map[java.lang.String, java.lang.String] = {
//    val javaMap = new java.util.HashMap[java.lang.String, java.lang.String]
//    map.foreach((m: (String, String)) => {
//      javaMap.put(m._1, m._2)
//    })
//    javaMap
//  }
//
}