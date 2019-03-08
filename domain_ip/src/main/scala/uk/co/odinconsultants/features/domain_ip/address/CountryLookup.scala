package uk.co.odinconsultants.features.domain_ip.address

import java.net.InetAddress

import com.maxmind.db.Reader

object CountryLookup {

  val inputStream = this.getClass.getClassLoader.getResourceAsStream("GeoLite2-Country.mmdb")

  val reader = new Reader(inputStream)

  /**
    * Returns the ISO code of the country to which this maps.
    */
  def apply(address: String): String = {
    val json = reader.get(InetAddress.getByName(address))
    json.get("country").get("iso_code").asText
  }

}
