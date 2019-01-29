package uk.co.odinconsultants.features.random


/**
  * See https://en.wikipedia.org/wiki/Kolmogorov_complexity
  */
object Kolmogorov {

  def score(x: String): Int = {
    import java.util.zip.Deflater
    val input = x.getBytes("UTF-8")
    val output = new Array[Byte](x.length)
    val compresser = new Deflater
    compresser.setInput(input)
    compresser.finish()
    val size = compresser.deflate(output)
    compresser.end()
    size
  }

  def averageScore(x: String): Double = score(x).toDouble / x.length

}
