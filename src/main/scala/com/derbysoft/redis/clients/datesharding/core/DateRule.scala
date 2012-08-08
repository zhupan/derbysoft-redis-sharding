package com.derbysoft.redis.clients.datesharding.core

trait DateRule {

  def getDate(key: String): String

}

class DefaultDateRule extends DateRule {

  private val dateLength = "2000-01-01".length()

  override def getDate(key: String): String = {
    key.substring(key.length() - dateLength)
  }

}
