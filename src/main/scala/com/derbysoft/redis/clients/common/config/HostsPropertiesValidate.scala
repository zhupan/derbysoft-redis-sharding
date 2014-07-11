package com.derbysoft.redis.clients.common.config

import java.util.regex.Pattern
import scala.collection.JavaConversions._

object HostsPropertiesValidate {

  def apply(hostMap: java.util.Map[String, String]) {
    val error = "Error: "
    if (hostMap.size != Hosts.redisHostsSize) {
      throw new Error(error + "Host size[" + hostMap.size + "] is illegal.")
    }
    hostMap.foreach((m: (String, String)) => {
      if (!m._1.startsWith(Hosts.hostPrefix)) {
        throw new Error(error + "Host prefix[" + m._1 + "] is illegal.")
      }
      if (m._1.length != Hosts.hostPrefix.length + 3) {
        throw new Error(error + "Host key[" + m._1 + "] is illegal.")
      }
      val hostNumber = m._1.substring(Hosts.hostPrefix.length).toInt
      if (0 > hostNumber || hostNumber > Hosts.redisHostsSize - 1) {
        throw new Error(error + "Host key[" + m._1 + "] is illegal.")
      }
      val hostAndPort = m._2.split(":")
      val ipPattern = Pattern.compile("^([1-9]|[1-9]\\d|1\\d{2}|2[0-1]\\d|22[0-3])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}$")
      if (!ipPattern.matcher(hostAndPort(0)).matches() && !"localhost".equals(hostAndPort(0))) {
        throw new Error(error + "Host[" + m._2 + "] is illegal.")
      }
      if (hostAndPort(1).toInt > 65535) {
        throw new Error(error + "Host[" + m._2 + "] is illegal.")
      }
    })
  }

}
