package com.derbysoft.redis.util

import junit.framework.{TestCase, TestSuite, Test}
import org.junit.Assert
import java.util.ResourceBundle
import com.derbysoft.redis.clients.common.listener.RedisInit

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
    val map = PropertiesToMap(ResourceBundle.getBundle(RedisInit.redisConfigName))
    println(map)
    Assert.assertTrue(map.size == 32)
  }

}
