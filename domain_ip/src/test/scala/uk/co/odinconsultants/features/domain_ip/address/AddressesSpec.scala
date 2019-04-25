package uk.co.odinconsultants.features.domain_ip.address

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, WordSpec}

@RunWith(classOf[JUnitRunner])
class AddressesSpec extends WordSpec with Matchers {

  import Addresses._

  "IP address by itself" should {
    "be recognized as an IP address" in {
      isIPAddress("127.0.0.1") shouldBe true
    }
  }

  "IP address with port" should {
    "be recognized as an IP address" in {
      isIPAddress("127.0.0.1:38632") shouldBe true
    }
  }

  "Non numericals" should {
    "not be recognized as IP addresses" in {
      isIPAddress("x.y.z.w") shouldBe false
    }
  }

}
