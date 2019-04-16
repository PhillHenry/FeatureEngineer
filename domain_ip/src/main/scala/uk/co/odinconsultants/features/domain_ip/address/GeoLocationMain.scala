package uk.co.odinconsultants.features.domain_ip.address

import scala.io.Source

object GeoLocationMain {

  def main(args: Array[String]): Unit = {
    val ipAddresses = Source.fromFile(args(0)).getLines()
    ipAddresses.foreach { ip =>
      val country   = CountryLookup(ip)
      val location  = CityLookup(ip)
      println("%-20s%-5s%30s".format(ip,
        country.right.getOrElse(""),
        location.right.map(x => "Lat = %3f   Long = %11.6f".format(x.lat, x.long)).right.getOrElse(""))
      )
    }
  }

}
