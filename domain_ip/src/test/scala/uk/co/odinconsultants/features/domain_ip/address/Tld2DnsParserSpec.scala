package uk.co.odinconsultants.features.domain_ip.address

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, WordSpec}

@RunWith(classOf[JUnitRunner])
class Tld2DnsParserSpec extends WordSpec with Matchers {

  import Tld2DnsParser._

  "JSON" should {
    "be transformed to a map" in {
      val json = """{
                   |  "abb": {
                   |    "_type": "newgtld",
                   |    "adapter": "none"
                   |  },
                   |  "abbott": {
                   |    "_group": "afiliassrs",
                   |    "_type": "newgtld",
                   |    "host": "whois.afilias-srs.net"
                   |  },
                   |  "abbvie": {
                   |    "_group": "afiliassrs",
                   |    "_type": "newgtld",
                   |    "host": "whois.afilias-srs.net"
                   |  }
                   |}
                   |""".stripMargin
      val map = parse(json).right.get

      map should have size 2
      map("abbott") shouldBe "whois.afilias-srs.net"
      map("abbvie") shouldBe "whois.afilias-srs.net"
    }
  }

}
