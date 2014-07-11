package com.derbysoft.redis.util

import scala.collection.immutable.TreeMap


object MapToTreeMap {

  def apply(map: scala.collection.mutable.Map[String, String]): TreeMap[String, String] = {
    TreeMap(map.toArray: _*)
  }
}
