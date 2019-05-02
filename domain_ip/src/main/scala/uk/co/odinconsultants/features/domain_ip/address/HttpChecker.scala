package uk.co.odinconsultants.features.domain_ip.address

import java.net.{HttpURLConnection, URL}

object HttpChecker {

  val protocol = "https://"

  def clean(x: String): String = {
    val reg = """(?:http|https)://(.*)""".r
    x match {
      case reg(y) => s"${protocol}${y}"
      case _      => s"${protocol}$x"
    }
  }

  def httpCodeOf(x: String): Int = {
    httpCodeCalling(clean(x))
  }

  def httpCodeCalling(httpURL: String): Int = {
    val url = new URL(httpURL)
    val con = url.openConnection().asInstanceOf[HttpURLConnection]
    con.getResponseCode
  }

}
