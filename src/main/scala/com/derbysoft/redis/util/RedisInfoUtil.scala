package com.derbysoft.redis.util

import java.util.regex.Pattern

object RedisInfoUtil {

  def getUsedMemory(info: String): Long = {
    val value = getMatch(info, "used_memory:", "\n")
    if (value == "") return 0
    value.toLong
  }

  private def getMatch(value: String, start: String, end: String): String = {
    val matcher = Pattern.compile("(" + start + ")(.+?)(" + end + ")").matcher(value)
    if (matcher.find()) {
      return matcher.group(2).trim
    }
    ""
  }

}
