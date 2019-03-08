package uk.co.odinconsultants.features.domain_ip.address

import org.scalatest.{Matchers, WordSpec}

class CityLookupSpec extends WordSpec with Matchers {

  "217.44.226.69" should {
    "map to Lonodn according to ipstack.com" in {
      CityLookup("217.44.226.69").fold(throw _, { location =>
        location.lat shouldBe 51.4439f +- 0.1f
        location.long shouldBe -0.1854f +- 0.1f
      })
    }
  }

}
