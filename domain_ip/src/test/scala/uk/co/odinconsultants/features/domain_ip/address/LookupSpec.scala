package uk.co.odinconsultants.features.domain_ip.address

import org.scalatest.{Matchers, WordSpec}

class LookupSpec extends WordSpec with Matchers {

  "The country of the BBC servers" should {
    "be the the US (according to ipstack.com)" in {
      Lookup("151.101.128.81") shouldBe "US"
    }
  }

}
