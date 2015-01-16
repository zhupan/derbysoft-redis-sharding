package com.derbysoft.redis.util

import java.util.ResourceBundle
import com.derbysoft.redis.clients.common.listener.RedisInit

object PropertiesToMap {

  def apply(properties: ResourceBundle): Map[String, String] = {
    val map = scala.collection.mutable.Map.empty[String, String]
    val keys = properties.getKeys
    while (keys.hasMoreElements) {
      val key = keys.nextElement()
      if (!key.contains(RedisInit.redisHostsSizeKey) && !key.contains(RedisInit.redisHostsSortKey)) {
        map.put(key.toString, properties.getString(key))
      }
    }
    map.toMap[String, String]
  }

}