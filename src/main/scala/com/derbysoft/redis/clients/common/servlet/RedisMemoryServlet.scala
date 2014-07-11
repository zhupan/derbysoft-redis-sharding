package com.derbysoft.redis.clients.common.servlet

import javax.servlet.http.{HttpServletRequest, HttpServlet, HttpServletResponse}
import com.derbysoft.redis.clients.ShardingRedis
import redis.clients.jedis.{Jedis, ShardedJedis}
import com.derbysoft.redis.util.{MapToTreeMap, RedisInfoUtil}
import scala.BigDecimal
import math.BigDecimal.RoundingMode

class RedisMemoryServlet extends HttpServlet {

  private val kilo = 1024d

  //(bit).Byte (B).KiloByte (KB).MegaByte (MB).GigaByte (GB).TeraByte (TB).PetaByte (PB).ExaByte (EB).ZetaByte (ZB).YottaByte (YB).NonaByte (NB).DoggaByte (DB)
  var units = List("B", "K", "M", "G", "T", "P", "E", "Z", "Y", "N", "D")

  override def doGet(request: HttpServletRequest, response: HttpServletResponse) {
    response.setContentType("text/html;charset=UTF-8")
    response.setStatus(HttpServletResponse.SC_OK)
    val host = request.getParameter("host")
    if (host != null && !host.equals("")) {
      val jedis = getJedis(host)
      response.getOutputStream.println("host:" + getHost(jedis))
      response.getOutputStream.println(jedis.info())
      return
    }
    var totalMemory: Double = ShardingRedis.single.usedMemory()
    var index = 0
    while (totalMemory > kilo && index < units.size - 1) {
      index += 1
      totalMemory /= kilo
    }
    response.getOutputStream.println("<div>Total Used Memory: " + BigDecimal.valueOf(totalMemory).setScale(3, RoundingMode.HALF_UP) + units(index) + "</div>")
    MapToTreeMap(ShardingRedis.hostsMap).foreach((h: (String, String)) => {
      printAllHosts(response, request, h)
    })
  }


  private def getHost(jedis: Jedis): String = {
    jedis.getClient.getHost + ":" + jedis.getClient.getPort
  }

  private def getJedis(host: String): Jedis = {
    val shardedJedis = ShardingRedis.single.getResource.asInstanceOf[ShardedJedis]
    try {
      val iterator = shardedJedis.getAllShards.iterator()
      while (iterator.hasNext) {
        val jedis = iterator.next()
        if (getHost(jedis).equals(host)) {
          return jedis
        }
      }
    }
    finally {
      ShardingRedis.single.returnResource(shardedJedis)
    }
    null
  }

  private def printAllHosts(response: HttpServletResponse, request: HttpServletRequest, h: (String, String)) {
    val host = h._2
    val jedis = getJedis(host)
    if (jedis == null) {
      throw new IllegalStateException("host[" + host + "] is null.")
    }
    val memory = RedisInfoUtil.getUsedMemoryHuman(jedis.info())
    response.getOutputStream.println("<div>")
    response.getOutputStream.println(host + " ")
    response.getOutputStream.println("<a href=\"" + request.getRequestURI + "?host=" + host + "\">" + memory + "</a>")
    response.getOutputStream.println("<br />")
    response.getOutputStream.println("</div>")
  }

  override def doPost(request: HttpServletRequest, response: HttpServletResponse) {
    doGet(request, response)
  }

}
