package com.derbysoft.redis.util

import java.util.Properties
import java.io.{StringReader, FileInputStream}

object PropertiesToMap {

  def apply(fileName: String): Map[String, String] = {
    val properties = new Properties()
    properties.load(new FileInputStream(fileName))
    properties.list(System.out)
    propertiesToMap(properties)
  }

  def stringToMap(value: String): Map[String, String] = {
    val properties = new Properties()
    properties.load(new StringReader(value))
    propertiesToMap(properties)
  }

  private def propertiesToMap(properties: Properties): Map[String, String] = {
    val map = scala.collection.mutable.Map.empty[String, String]
    val keys = properties.keys()
    while (keys.hasMoreElements) {
      val key = keys.nextElement()
      map.put(key.toString, properties.get(key).toString)
    }
    map.toMap[String, String]
  }


}