package com.derbysoft.redis.clients

import com.derbysoft.redis.util.{RichFile, MapToProperties, PropertiesToMap}
import common.config.HostsPropertiesValidate
import hashsharding.core.{ShardedJedisClientPool, HashShardingRedis}
import scala.collection.JavaConversions._
import org.apache.commons.logging.LogFactory
import scala.collection.mutable

object ShardingRedis {

  var hostsMap: scala.collection.mutable.Map[String, String] = new mutable.HashMap[String, String] {
    seedvalue = 0
  }

  val single = new HashShardingRedis

  private var hostsFile = ""

  private val logger = LogFactory.getLog(this.getClass)

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
      ShardedJedisClientPool.rePool()
    } catch {
      case e: Exception =>
        logger.error(e.getMessage, e)
        return e.getMessage
    }
    hosts
  }

}



