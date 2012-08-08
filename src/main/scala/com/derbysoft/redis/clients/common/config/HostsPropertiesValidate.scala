package com.derbysoft.redis.clients.common.config

import java.util.regex.Pattern

object HostsPropertiesValidate {

  def apply(hostMap: Map[String, String]) {
    val error = "Error: "
    if (hostMap.size != HostKey.redisHostsSize) {
      throw new IllegalArgumentException(error + "Host size[" + hostMap.size + "] is illegal.")
    }
    hostMap.foreach((m: (String, String)) => {
      if (!m._1.startsWith(HostKey.hostPrefix)) {
        throw new IllegalArgumentException(error + "Host prefix[" + m._1 + "] is illegal.")
      }
      if (m._1.length != HostKey.hostPrefix.length + 3) {
        throw new IllegalArgumentException(error + "Host key[" + m._1 + "] is illegal.")
      }
      val hostNumber = m._1.substring(HostKey.hostPrefix.length).toInt
      if (0 > hostNumber || hostNumber > 31) {
        throw new IllegalArgumentException(error + "Host key[" + m._1 + "] is illegal.")
      }
      val hostAndPort = m._2.split(":")
      val ipPattern = Pattern.compile("^([1-9]|[1-9]\\d|1\\d{2}|2[0-1]\\d|22[0-3])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}$")
      if (!ipPattern.matcher(hostAndPort(0)).matches() && !"localhost".equals(hostAndPort(0))) {
        throw new IllegalArgumentException(error + "Host[" + m._2 + "] is illegal.")
      }
      if (hostAndPort(1).toInt > 65535) {
        throw new IllegalArgumentException(error + "Host[" + m._2 + "] is illegal.")
      }
    })
  }

}
