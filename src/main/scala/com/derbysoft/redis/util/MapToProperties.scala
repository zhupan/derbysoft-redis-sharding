package com.derbysoft.redis.util

import scala.collection.immutable.TreeMap

object MapToProperties {

  def apply(map: Map[String, String]): String = {
    val result = new StringBuffer
    val keys = getKeys(map)
    keys.foreach(key => {
      result.append(key + "=" + map(key))
      result.append("\n")
    })
    result.toString
  }

  private def getKeys(map: Map[String, String]): List[String] = {
    TreeMap(map.toArray: _*).keys.toList
  }
}
