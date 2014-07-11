package com.derbysoft.redis.util

import junit.framework.{TestCase, TestSuite, Test}
import org.junit.Assert
import redis.clients.util.Hashing

object MurmurHashTest {
  def suite: Test = {
    val suite = new TestSuite(classOf[MurmurHashTest])
    suite
  }

  def main(args: Array[String]) {
    junit.textui.TestRunner.run(suite)
  }
}

class MurmurHashTest extends TestCase("app") {

  def testGetUsedMemory() {
    Assert.assertEquals("6594452278845490308", String.valueOf(Hashing.MURMUR_HASH.hash("key:10.200.107.10:9001")))
    Assert.assertEquals("-1498251713141396620", String.valueOf(Hashing.MURMUR_HASH.hash("key:10.200.107.10:9002")))
    Assert.assertEquals("5865537198882355028", String.valueOf(Hashing.MURMUR_HASH.hash("key:10.200.107.10:9003")))
    Assert.assertEquals("9111319311765554970", String.valueOf(Hashing.MURMUR_HASH.hash("key:10.200.107.10:9004")))
    Assert.assertEquals("1820861874942010498", String.valueOf(Hashing.MURMUR_HASH.hash("key:10.200.107.10:9005")))
    Assert.assertEquals("-1128843832583896252", String.valueOf(Hashing.MURMUR_HASH.hash("key:10.200.107.10:9006")))
    Assert.assertEquals("-3618849117625022224", String.valueOf(Hashing.MURMUR_HASH.hash("key:10.200.107.10:9007")))
    Assert.assertEquals("2540465882451231453", String.valueOf(Hashing.MURMUR_HASH.hash("key:10.200.107.10:9008")))
  }

}
