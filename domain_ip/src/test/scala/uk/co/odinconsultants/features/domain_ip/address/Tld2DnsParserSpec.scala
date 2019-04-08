package uk.co.odinconsultants.features.domain_ip.address

import org.scalatest.{Matchers, WordSpec}

class Tld2DnsParserSpec extends WordSpec with Matchers {

  import Tld2DnsParser._

  "JSON" should {
    "be transformed to a map" in {
      val json = """{
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
