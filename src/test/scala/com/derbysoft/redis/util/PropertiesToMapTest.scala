package com.derbysoft.redis.util

import java.util.Properties

import com.derbysoft.redis.clients.common.listener.RedisInit
import junit.framework.{Test, TestCase, TestSuite}
import org.junit.Assert

object PropertiesToMapTest {
  def suite: Test = {
    val suite = new TestSuite(classOf[PropertiesToMapTest])
    suite
  }

  def main(args: Array[String]) {
    junit.textui.TestRunner.run(suite)
  }
}

class PropertiesToMapTest extends TestCase("app") {

  def test() {
    val properties = new Properties()
    val inputStream = this.getClass.getClassLoader.getResourceAsStream(RedisInit.redisConfigName)
    properties.load(inputStream)
    val map = PropertiesToMap(properties)
    println(map)
    Assert.assertTrue(map.size == 32)
  }

}
