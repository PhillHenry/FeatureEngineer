package uk.co.odinconsultants.features.random


/**
  * See https://en.wikipedia.org/wiki/Kolmogorov_complexity
  */
object Kolmogorov {

  /**
    * "Kolmogorov complexity is not a computable function!"
    * https://stackoverflow.com/questions/2979174/how-do-i-compute-the-approximate-entropy-of-a-bit-string/2979208
    */
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
