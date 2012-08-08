package com.derbysoft.redis.clients.datesharding.core

import com.derbysoft.redis.clients.common.config.HostRule
import com.derbysoft.redis.clients.ShardingRedis

object GetHost {

  val rule = new DateHostRule

  def apply(key: String): String = {
    rule.getHost(key)
  }

}

protected class DateHostRule extends HostRule {

  val dateRule = new DefaultDateRule

  override def getHost(key: String): String = {
    val date = dateRule.getDate(key)
    ShardingRedis.hostsMap.get(DateHostKey(date)) match {
      case Some(x) => return x
      case None => throw new RuntimeException("Redis host is not found by date[" + date + "].")
    }
  }

}
