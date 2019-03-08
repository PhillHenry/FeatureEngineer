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

  "Bad lookups" should {
    "be gracefully handle IPv6" in {
      CountryLookup("FE80:0000:0000:0000:0202:B3FF:FE1E:8329").isLeft shouldBe true
    }
  }

  "IPv6" should {
    "gracefully also be a possibility (results from ipstack.com)" in {
      CountryLookup("2001:2b8:0000:0000:0202:B3FF:FE1E:8329") shouldBe Right("KR")
    }
  }

}
