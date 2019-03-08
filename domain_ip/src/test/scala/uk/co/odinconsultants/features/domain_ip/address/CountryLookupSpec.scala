package uk.co.odinconsultants.features.domain_ip.address

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, WordSpec}

@RunWith(classOf[JUnitRunner])
class CountryLookupSpec extends WordSpec with Matchers {

  "The country of the BBC servers" should {
    "be the the US (according to ipstack.com)" in {
      CountryLookup("151.101.128.81") shouldBe Right("US")
    }
  }

  "IPv6" should {
    "gracefully handle IPv6" in {
      CountryLookup("FE80:0000:0000:0000:0202:B3FF:FE1E:8329").isLeft shouldBe true
    }
  }

}
