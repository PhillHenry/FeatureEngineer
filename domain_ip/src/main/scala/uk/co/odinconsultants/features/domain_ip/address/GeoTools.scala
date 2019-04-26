package uk.co.odinconsultants.features.domain_ip.address

import uk.co.odinconsultants.features.domain_ip.address.CityLookup.GeoLocation

trait GeoTools {

  val NoGeoLocation: GeoLocation = GeoLocation(-999, -999)

  val NoDataString = "-"

}
