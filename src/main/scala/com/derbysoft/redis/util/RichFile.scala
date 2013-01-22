package com.derbysoft.redis.util

import java.io._
import scala.io._

class RichFile(fileName: String) {

  def text = Source.fromFile(fileName).mkString

  def text_=(data: String) {
    val out = new PrintWriter(fileName)
    try {
      out.print(data)
    }
    finally {
      out.close()
    }
  }
}

object RichFile {

  implicit def enrichFile(file: String) = new RichFile(file)

  def writeStringToFile(value: String, fileName: String) {
    val file = new RichFile(fileName)
    file.text = value
  }

}

