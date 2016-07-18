package com.derbysoft.redis.util

import java.util.Properties

import com.derbysoft.redis.clients.common.listener.RedisInit

object PropertiesToMap {

  def apply(properties: Properties): Map[String, String] = {
    val map = scala.collection.mutable.Map.empty[String, String]
    val keys = properties.keys()
    while (keys.hasMoreElements) {
      val key = keys.nextElement()
      if (!key.toString.contains(RedisInit.redisHostsSizeKey) && !key.toString.contains(RedisInit.redisHostsSortKey)) {
        map.put(key.toString, properties.get(key).toString)
      }
    }
    map.toMap[String, String]
  }

}