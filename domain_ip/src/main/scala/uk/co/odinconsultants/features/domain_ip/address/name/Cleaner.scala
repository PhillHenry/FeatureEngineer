package uk.co.odinconsultants.features.domain_ip.address.name

import uk.co.odinconsultants.features.domain_ip.address.name.Comparison.{A, E, F, O}
import uk.co.odinconsultants.features.domain_ip.util.Reading

object Cleaner {

  val ascii:          Seq[Char]         = ('0' to '9') ++ ('a' to 'z') ++ ('A' to 'Z') :+ '.' :+ '-'
  val asciiUnicde:    Set[String]       = ascii.map(_.toString).toSet
  val unicode2Ascii:  Map[String, Char] = ascii.map(x => x.toString -> x).toMap

  val x2Ascii: Map[String, Char] = Reading.fromClasspath("char_codes.txt").flatMap { line =>
    val xs    = line.split(",")
    val aOpt  = xs.find(asciiUnicde.contains(_))
    mappings(aOpt, xs)
  }.toMap

  def mappings(aOpt: Option[String], xs: Seq[String]): Seq[(String, Char)] =
    aOpt.map { a =>
      xs.filterNot(_ == a).map(_ -> unicode2Ascii(a))
    }.toList.flatten

  def toSimilarAscii(xs: String): String = {
    xs.map { x =>
      x2Ascii.getOrElse(x.toString, x)
    }
  }

  def clean(x: String): String = {
    val ascii = if (x.startsWith("xn--")) java.net.IDN.toUnicode(x) else x
    println(s"x = $x, ascii = $ascii")
    ascii.replaceAll(A, "a").replaceAll(O, "o").replaceAll(F, "f").replaceAll(E, "e").toLowerCase
  }

}
