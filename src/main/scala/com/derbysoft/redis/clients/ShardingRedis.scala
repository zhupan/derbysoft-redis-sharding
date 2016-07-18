package com.derbysoft.redis.clients

import java.util.Properties

import com.derbysoft.redis.clients.common.config.HostsPropertiesValidate
import com.derbysoft.redis.clients.hashsharding.core.{HashShardingRedis, ShardedJedisClientPool}
import com.derbysoft.redis.util.{MapToProperties, PropertiesToMap}

import scala.collection.JavaConversions._
import scala.collection.mutable

object ShardingRedis {

  var hostsMap: scala.collection.mutable.Map[String, String] = new mutable.HashMap[String, String] {
    seedvalue = 0
  }

  val single = new HashShardingRedis

  def init(properties: Properties): String = {
    updateHosts(PropertiesToMap(properties))
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



