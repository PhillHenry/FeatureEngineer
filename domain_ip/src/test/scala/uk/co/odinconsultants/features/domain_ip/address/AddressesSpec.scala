package uk.co.odinconsultants.features.domain_ip.address

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, WordSpec}

@RunWith(classOf[JUnitRunner])
class AddressesSpec extends WordSpec with Matchers {

  val tlds: Set[String] = Set("com",
    "googleapis.com",
    "s3.amazonaws.com",
    "goog",
    "tv",
    "elb.amazonaws.com",
    "appspot.com")

  import Addresses._

  "TLDs" should {
    val sortedTLDs = longestToShortest(tlds)
    "be sorted longest first" in {
      sortedTLDs shouldBe List("tv", "com", "goog", "appspot.com", "googleapis.com", "s3.amazonaws.com", "elb.amazonaws.com").reverse
    }
    "be stripped" in {
      removeTLD(sortedTLDs, s"x.y.z.${tlds.head}") shouldBe "z"
    }
  }

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
