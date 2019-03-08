package uk.co.odinconsultants.features.domain_ip.address

import java.net.InetAddress

import uk.co.odinconsultants.features.domain_ip.address.Lookup._

object CountryLookup {

  val reader = readerOf("GeoLite2-Country.mmdb")

  /**
    * Returns the ISO code of the country to which this maps.
    */
  def apply(address: String): Either[Throwable, String] = {

    def fn: String = {
      val json = reader.get(InetAddress.getByName(address))
      json.get("country").get("iso_code").asText
    }

    tryLookup(fn)
  }

}
