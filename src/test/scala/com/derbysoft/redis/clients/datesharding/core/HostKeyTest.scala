package com.derbysoft.redis.clients.datesharding.core

import junit.framework._
import java.util.Date
import com.derbysoft.redis.clients.common.config.HostKey

object HostKeyTest {
  def suite: Test = {
    val suite = new TestSuite(classOf[HostKeyTest]);
    suite
  }

  def main(args: Array[String]) {
    junit.textui.TestRunner.run(suite);
  }
}

class HostKeyTest extends TestCase("app") {

  def testDestinationHosts = {
    Assert.assertEquals(HostKey.hostPrefix + "031", DateHostKey("2012-01-01"))
    Assert.assertEquals(HostKey.hostPrefix + "000", DateHostKey("2012-01-02"))
    Assert.assertEquals(HostKey.hostPrefix + "001", DateHostKey("2012-01-03"))
    Assert.assertEquals(HostKey.hostPrefix + "002", DateHostKey("2012-01-04"))
    Assert.assertEquals(HostKey.hostPrefix + "003", DateHostKey("2012-01-05"))
    Assert.assertEquals(HostKey.hostPrefix + "004", DateHostKey("2012-01-06"))
    Assert.assertEquals(HostKey.hostPrefix + "005", DateHostKey("2012-01-07"))
    Assert.assertEquals(HostKey.hostPrefix + "006", DateHostKey("2012-01-08"))
    Assert.assertEquals(HostKey.hostPrefix + "024", DateHostKey("2012-05-01"))
    Assert.assertEquals(HostKey.hostPrefix + "025", DateHostKey("2012-05-02"))
    Assert.assertEquals(HostKey.hostPrefix + "026", DateHostKey("2012-05-03"))
    Assert.assertEquals(HostKey.hostPrefix + "027", DateHostKey("2012-05-04"))
    Assert.assertEquals(HostKey.hostPrefix + "028", DateHostKey("2012-05-05"))
    Assert.assertEquals(HostKey.hostPrefix + "029", DateHostKey("2012-05-06"))
    Assert.assertEquals(HostKey.hostPrefix + "030", DateHostKey("2012-05-07"))
    Assert.assertEquals(HostKey.hostPrefix + "031", DateHostKey("2012-05-08"))
    Assert.assertEquals(HostKey.hostPrefix + "022", DateHostKey("2012-05-31"))
    Assert.assertEquals(HostKey.hostPrefix + "026", DateHostKey("2012-04-01"))
    Assert.assertEquals(HostKey.hostPrefix + "027", DateHostKey("2012-04-02"))
    Assert.assertEquals(HostKey.hostPrefix + "028", DateHostKey("2012-04-03"))
    Assert.assertEquals(HostKey.hostPrefix + "029", DateHostKey("2012-04-04"))
    Assert.assertEquals(HostKey.hostPrefix + "030", DateHostKey("2012-04-05"))
    Assert.assertEquals(HostKey.hostPrefix + "031", DateHostKey("2012-04-06"))
    Assert.assertEquals(HostKey.hostPrefix + "000", DateHostKey("2012-04-07"))
    Assert.assertEquals(HostKey.hostPrefix + "001", DateHostKey("2012-04-08"))
    Assert.assertEquals(HostKey.hostPrefix + "002", DateHostKey("2012-04-09"))
  }

  def test = {

    val start = new Date();
    for (i <- 1 to 10000) {
      DateHostKey("2012-01-01")
      DateHostKey("2012-04-07")
      DateHostKey("2012-04-05")
    }
    println(new Date().getTime - start.getTime)
  }

}
