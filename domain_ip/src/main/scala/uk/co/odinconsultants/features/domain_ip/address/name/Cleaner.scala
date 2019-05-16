package uk.co.odinconsultants.features.domain_ip.address.name

import uk.co.odinconsultants.features.domain_ip.address.name.Comparison.{A, E, F, O}

object Cleaner {

  def clean(x: String): String = {
    val ascii = if (x.startsWith("xn--")) java.net.IDN.toUnicode(x) else x
    println(s"x = $x, ascii = $ascii")
    ascii.replaceAll(A, "a").replaceAll(O, "o").replaceAll(F, "f").replaceAll(E, "e").toLowerCase
  }

}
