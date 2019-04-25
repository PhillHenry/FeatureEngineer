package uk.co.odinconsultants.features.domain_ip.address

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, WordSpec}

@RunWith(classOf[JUnitRunner])
class AddressesSpec extends WordSpec with Matchers {

  import Addresses._

  "hostname with port" should {
    val x = "bbc.co.uk"
    "have its port removed" in {
      removePorts(s"$x:8080") shouldBe x
    }
  }

  "IP address by itself" should {
    "be recognized as an IP address" in {
      isIPAddress("127.0.0.1") shouldBe true
    }
  }

  "IP address with port" should {
    "be recognized as an IP address" in {
      isIPAddress("127.0.0.1:38632") shouldBe true
    }
    "have its port removed" in {
      removePorts("127.0.0.1:38632") shouldBe "127.0.0.1"
    }
  }

  "Non numericals" should {
    "not be recognized as IP addresses" in {
      isIPAddress("x.y.z.w") shouldBe false
    }
  }

}
