package uk.co.odinconsultants.features.domain_ip.address

import java.net.{HttpURLConnection, URL}

object HttpChecker {

  val https = "https://"
  val http  = "http://"

  def clean(x: String, port: Int): String =
    if (port == 80) matchProtocol(x, http) else ensureHttps(x)

  def ensureHttps(x: String): String =
    matchProtocol(x, https)

  private def matchProtocol(x: String, protocol: String) = {
    val reg = """(?:http|https)://(.*)""".r
    x match {
      case reg(y) => s"${protocol}${y}"
      case _      => s"${protocol}$x"
    }
  }

  def httpCodeOf(x: String, port: Int): Int =
    httpCodeCalling(clean(x, port))

  def httpsCodeOf(x: String): Int =
    httpCodeCalling(ensureHttps(x))

  def httpCodeCalling(httpURL: String): Int = {
    val url = new URL(httpURL)
    val con = url.openConnection().asInstanceOf[HttpURLConnection]
    con.getResponseCode
  }

}
