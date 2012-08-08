package com.derbysoft.redis.clients

import com.derbysoft.redis.util.{RichFile, MapToProperties, PropertiesToMap}
import common.config.HostsPropertiesValidate
import hashsharding.core.{HashShardedJedisPool, HashShardingRedis}

object ShardingRedis {

  val hostsMap: scala.collection.mutable.Map[String, String] = scala.collection.mutable.Map[String, String]()

  val single = new HashShardingRedis

  private var hostsFile = ""

  def init(hostsFile: String): String = {
    this.hostsFile = hostsFile
    updateHosts(PropertiesToMap(hostsFile))
    hosts
  }

  def hosts: String = MapToProperties(hostsMap.toMap[String, String])

  def updateHosts(hostsProperties: String): String = updateHosts(PropertiesToMap.stringToMap(hostsProperties))

  private def updateHosts(map: Map[String, String]): String = {
    try {
      HostsPropertiesValidate(map)
      hostsMap.clear()
      hostsMap.++=(map.toList)
      RichFile.writeStringToFile(hosts, hostsFile)
      HashShardedJedisPool.initShards()
    } catch {
      case e: Exception => {
        println(e.getMessage)
        return e.getMessage
      }
    }
    hosts
  }

}



