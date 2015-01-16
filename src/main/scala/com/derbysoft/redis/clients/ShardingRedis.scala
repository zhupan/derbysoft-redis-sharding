package com.derbysoft.redis.clients

import com.derbysoft.redis.util.{MapToProperties, PropertiesToMap}
import common.config.HostsPropertiesValidate
import hashsharding.core.{ShardedJedisClientPool, HashShardingRedis}
import scala.collection.JavaConversions._
import scala.collection.mutable
import java.util.ResourceBundle

object ShardingRedis {

  var hostsMap: scala.collection.mutable.Map[String, String] = new mutable.HashMap[String, String] {
    seedvalue = 0
  }

  val single = new HashShardingRedis

  def init(config: ResourceBundle): String = {
    updateHosts(PropertiesToMap(config))
    hosts
  }

  def hosts: String = MapToProperties(hostsMap.toMap[String, String])

  private def updateHosts(map: Map[String, String]): String = {
    try {
      HostsPropertiesValidate(map)
      hostsMap.clear()
      hostsMap.++=(map.toList)
      ShardedJedisClientPool.rePool()
    } catch {
      case e: Exception =>
        println(e.getMessage, e)
        return e.getMessage
    }
    hosts
  }

}



