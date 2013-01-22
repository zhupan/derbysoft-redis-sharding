package com.derbysoft.redis.util

object MapToProperties {

  def apply(map: Map[String, String]): String = {
    val result = new StringBuffer
    val keys = map.keys.toList.sortWith(_ < _)
    keys.foreach(key => {
      result.append(key + "=" + map(key))
      result.append("\n")
    })
    result.toString
  }

}
