package uk.co.odinconsultants.features.domain_ip.address

import com.maxmind.db.Reader
import uk.co.odinconsultants.features.domain_ip.address.Lookup.readerOf

object Readers {

  val reader: Reader = readerOf("GeoLite2-Country.mmdb")

}
