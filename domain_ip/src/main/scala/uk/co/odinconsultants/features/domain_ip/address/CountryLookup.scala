package uk.co.odinconsultants.features.domain_ip.address

import java.net.InetAddress

import com.maxmind.db.Reader
import uk.co.odinconsultants.features.domain_ip.address.Lookup._

object CountryLookup extends GeoTools {

  def lookupCountry(ip: String): String = {
    val either = CountryLookup.apply(ip, Readers.reader)
    either.right.getOrElse(NoDataString)
  }

  /**
    * Returns the ISO code of the country to which this maps.
    */
  def apply(address: String, reader: Reader): Either[Throwable, String] = {

    def fn: String = {
      val json = reader.get(InetAddress.getByName(address))
      json.get("country").get("iso_code").asText
    }

    tryLookup(fn)
  }

}
