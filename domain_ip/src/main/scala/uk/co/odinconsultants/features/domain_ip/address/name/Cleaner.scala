package uk.co.odinconsultants.features.domain_ip.address.name

import uk.co.odinconsultants.features.domain_ip.address.name.Comparison.{A, E, F, O}
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
    val aOpt  = xs.find(asciiUnicde.contains)
    mappings(aOpt, xs)
  }

  def mappings(aOpt: Option[String], xs: Seq[String]): Seq[(String, Char)] =
    aOpt.map { a =>
      xs.filterNot(_ == a).map(_ -> unicode2Ascii(a))
    }.toList.flatten

  val x2Ascii: Map[String, Char] = Reading.fromClasspath("char_codes.txt").flatMap (csv2Mappings).toMap

  def toSimilarAscii(xs: String): String =
    xs.map { x =>
      x2Ascii.getOrElse(x.toString, x)
    }

  def clean(x: String): String = {
    val ascii = if (x.startsWith("xn--")) java.net.IDN.toUnicode(x) else x
    println(s"x = $x, ascii = $ascii")
    ascii.replaceAll(A, "a").replaceAll(O, "o").replaceAll(F, "f").replaceAll(E, "e").toLowerCase
  }

}
