package uk.co.odinconsultants.features.domain_ip.address

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, WordSpec}

@RunWith(classOf[JUnitRunner])
class CountryLookupSpec extends WordSpec with Matchers {

  "The country of the BBC servers" should {
    "be the the US (according to ipstack.com)" in {
      CountryLookup("151.101.128.81") shouldBe "US"
    }
  }

}
