package uk.co.odinconsultants.features.random

import java.util.zip.Deflater

/**
  * See https://en.wikipedia.org/wiki/Kolmogorov_complexity
  */
object Kolmogorov {

  def score(x: String): Int = {
    val input = x.getBytes("UTF-8")
    val output = new Array[Byte](x.length)
    val compresser = new Deflater
    compresser.setInput(input)
    compresser.finish()
    compresser.deflate(output)
  }

}
