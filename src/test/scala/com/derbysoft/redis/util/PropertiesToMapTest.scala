package com.derbysoft.redis.util

import junit.framework.{TestCase, TestSuite, Test}
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

  val hosts = "redis.host.000=10.200.107.35:6379\nredis.host.001=10.200.107.35:6380\nredis.host.002=10.200.107.35:6379\nredis.host.003=10.200.107.35:6379\nredis.host.004=10.200.107.35:6379\nredis.host.005=10.200.107.35:6379\nredis.host.006=10.200.107.35:6379\nredis.host.007=10.200.107.35:6379\nredis.host.008=10.200.107.35:6379\nredis.host.009=10.200.107.35:6379\nredis.host.010=10.200.107.35:6379\nredis.host.011=10.200.107.35:6379\nredis.host.012=10.200.107.35:6379\nredis.host.013=10.200.107.35:6379\nredis.host.014=10.200.107.35:6379\nredis.host.015=10.200.107.35:6379\nredis.host.016=10.200.107.35:6379\nredis.host.017=10.200.107.35:6379\nredis.host.018=10.200.107.35:6379\nredis.host.019=10.200.107.35:6379\nredis.host.020=10.200.107.35:6379\nredis.host.021=10.200.107.35:6379\nredis.host.022=10.200.107.35:6379\nredis.host.023=10.200.107.35:6379\nredis.host.024=10.200.107.35:6379\nredis.host.025=10.200.107.35:6379\nredis.host.026=10.200.107.35:6379\nredis.host.027=10.200.107.35:6379\nredis.host.028=10.200.107.35:6379\nredis.host.029=10.200.107.35:6379\nredis.host.030=10.200.107.35:6379\nredis.host.031=10.200.107.35:6379"

  def test() {
    val map = PropertiesToMap.stringToMap(hosts)
    println(map)
    Assert.assertTrue(map.size == 32)
  }

}
