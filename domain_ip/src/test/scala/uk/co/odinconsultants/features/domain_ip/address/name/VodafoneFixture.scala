package uk.co.odinconsultants.features.domain_ip.address.name

import Comparison._
import Cleaner._

trait VodafoneFixture extends PunyCode {

  val vodafone                = "vodafone"
  val ns:       Set[Int]      = Set(3)

  def probabilities(x: String): Probabilities = probabilitiesWBlanks(ns, clean(x))
  val baseline: Probabilities = probabilities(vodafone)

  val notSimilar: Set[String]   = Set("vodoarquitectos", "espansionetv", "vimeocdn", "fondazionecannavaroferrara",
    "led-laboratorioeducazionedialogo", "voda", "icpavone")

  val vOdAFOnE  = s"v${O}d${A}${F}${O}n${E}"
  val similar: Set[String]     = Set("www-vodafone", "v0dafone", "vodaf0ne", s"v${O}dafone", s"vodaf${O}ne",
    s"v${O}daf${O}ne", s"vod${A}fone", s"vodafon${E}", s"v${O}d${A}f${O}n${E}", "vodaphone",
    s"voda${F}one", vOdAFOnE, "xn--vodafon-ehg")

  def asString(xs: Set[String]): String = {
    val ordered = xs.map(x => x -> compare(baseline, x, ns)).toList.sortBy(_._2)
    ordered.map { case (w, v) => "%40s%10.4f (%s)".format(w, v, clean(w))}.mkString("\n")
  }
}
