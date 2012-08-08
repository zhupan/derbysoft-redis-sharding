package com.derbysoft.redis.clients.datesharding.core

import java.util.Date
import com.derbysoft.redis.clients.common.config.HostKey

protected object DateHostKey {

  private val msOfDay = 3600 * 24 * 1000

  private val year2000 = parseDate("2000-01-01")

  private def parseDate(date: String): Date = {
    new Date(date.substring(0, 4).toInt - 1900, date.substring(5, 7).toInt - 1, date.substring(8).toInt)
  }

  def apply(date: String): String = {
    val days = (parseDate(date).getTime - year2000.getTime) / msOfDay
    val number = (days % HostKey.redisHostsSize + 1000).toString
    HostKey.hostPrefix + number.substring(1)
  }

}
