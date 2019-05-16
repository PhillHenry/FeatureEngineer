package uk.co.odinconsultants.features.domain_ip.util

import java.io.{BufferedReader, InputStreamReader}

import uk.co.odinconsultants.features.domain_ip.address.DomainNameRegistry

import scala.collection.mutable.ArrayBuffer

object Reading {

  def fromClasspath[T](file: String): List[String] = {
    val stream  = DomainNameRegistry.getClass.getClassLoader.getResourceAsStream(file)
    val buffer  = new BufferedReader(new InputStreamReader(stream))
    val output  = new ArrayBuffer[String]()
    var line    = buffer.readLine()
    while (line != null) {
      output += line
      line    = buffer.readLine()
    }
    stream.close()
    output.toList
  }

}
