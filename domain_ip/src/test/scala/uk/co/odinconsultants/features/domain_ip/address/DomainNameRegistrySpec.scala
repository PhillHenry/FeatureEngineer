package uk.co.odinconsultants.features.domain_ip.address

import java.net.InetAddress

import com.google.common.base.Optional
import org.scalatest.{Matchers, WordSpec}
import org.thryft.native_.InternetDomainName

class DomainNameRegistrySpec extends WordSpec with Matchers {

  import DomainNameRegistry._

  "Google optional" should {
    "map to Scala" in {
      toOption(Optional.of("x")) shouldBe Some("x")
      toOption(Optional.absent()) shouldBe None
    }
  }

  "An attempt to parse" should {
    val badDomain = InternetDomainName.from("x")
    val goodDNS   = InetAddress.getByName("whois.networksolutions.com")
    "yield none for bad domains" in {
      attemptParse(badDomain, goodDNS) shouldBe Optional.absent()
    }
  }

}
