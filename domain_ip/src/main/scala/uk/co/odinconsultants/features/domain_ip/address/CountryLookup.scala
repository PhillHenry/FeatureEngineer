package uk.co.odinconsultants.features.domain_ip.address

import java.net.InetAddress

import com.maxmind.db.{CHMCache, Reader}

import scala.util.{Failure, Success, Try}

object CountryLookup {

  val inputStream = this.getClass.getClassLoader.getResourceAsStream("GeoLite2-Country.mmdb")

  val reader = new Reader(inputStream, new CHMCache())

  /**
    * Returns the ISO code of the country to which this maps.
    */
  def apply(address: String): Either[Throwable, String] = {
    val json = reader.get(InetAddress.getByName(address))
    Try { json.get("country").get("iso_code").asText } match {
      case Success(x) => Right(x)
      case Failure(x) => Left(x)
    }

  }

}
