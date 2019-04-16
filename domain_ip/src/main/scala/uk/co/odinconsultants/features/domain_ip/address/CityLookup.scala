package uk.co.odinconsultants.features.domain_ip.address

import java.net.InetAddress

import com.maxmind.db.Reader
import uk.co.odinconsultants.features.domain_ip.address.Lookup.{readerOf, tryLookup}

object CityLookup {

  val reader: Reader = readerOf("GeoLite2-City.mmdb")

  case class GeoLocation(lat: Float, long: Float)

  def apply(address: String): Either[Throwable, GeoLocation] = {

    def fn: GeoLocation = {
      val json = reader.get(InetAddress.getByName(address))
      val lat   = json.get("location").get("latitude").asText.toFloat
      val long  = json.get("location").get("longitude").asText.toFloat
      GeoLocation(lat, long)
    }

    tryLookup(fn)
  }

}
