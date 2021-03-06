package uk.co.odinconsultants.features.domain_ip.address.name

import uk.co.odinconsultants.features.domain_ip.util.Reading

object Cleaner {

  val domainAscii:    Seq[Char]         = ('0' to '9') ++ ('a' to 'z') ++ ('A' to 'Z') :+ '.' :+ '-'
  val asciiUnicde:    Set[String]       = domainAscii.map(_.toString).toSet
  val unicode2Ascii:  Map[String, Char] = domainAscii.map(x => x.toString -> x).toMap

  def int2Unicode(i: Int): String =
    Character.toString(i.toChar)

  def csv2Unicode(line: String): Seq[String] = if (line.startsWith("#")) Seq() else
    line.split(",").map(x => Integer.parseInt(x, 16)).map(int2Unicode)

  val csv2Mappings: String => Seq[(String, Char)] = { line =>
    val xs    = csv2Unicode(line)
    val aOpt  = xs.reverse.find(asciiUnicde.contains)
    mappings(aOpt, xs)
  }

  def mappings(aOpt: Option[String], xs: Seq[String]): Seq[(String, Char)] =
    aOpt.map { a =>
      xs.filterNot(_ == a).filterNot(asciiUnicde.contains(_)).map(_ -> unicode2Ascii(a))
    }.toList.flatten

  val x2Ascii: Map[String, Char] = Reading.fromClasspath("char_codes.txt").flatMap (csv2Mappings).toMap

  /**
    * TODO: this should really return Set[String] as there are potentially more than one possible mappings.
    */
  def toSimilarAscii(xs: String): String =
    xs.map { x =>
      x2Ascii.getOrElse(x.toString, x)
    }

  def toAscii(x: String): String = if (x.startsWith("xn--")) java.net.IDN.toUnicode(x) else x

  def clean(x: String): String = {
    val ascii = toAscii(x)
    toSimilarAscii(ascii).toLowerCase
  }

}
