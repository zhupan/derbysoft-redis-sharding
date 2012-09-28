package com.derbysoft.redis.util

import java.util.regex.Pattern

object RedisInfoUtil {

  def getUsedMemory(info: String): Long = {
    val value = getMatch(info, "used_memory:", "\r")
    if (value.equals("")) return 0
    value.toLong
  }

  def getUsedMemoryHuman(info: String): String = {
    val value = getMatch(info, "used_memory_human:", "\r")
    if (value == null || value.equals("")) {
      return "0M"
    }
    value
  }

  private def getMatch(value: String, start: String, end: String): String = {
    val matcher = Pattern.compile("(" + start + ")(.+?)(" + end + ")").matcher(value)
    if (matcher.find()) {
      return matcher.group(2).trim
    }
    ""
  }

}
