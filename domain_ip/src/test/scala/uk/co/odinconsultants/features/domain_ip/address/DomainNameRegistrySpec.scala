package uk.co.odinconsultants.features.domain_ip.address

import com.google.common.base.Optional
import org.scalatest.{Matchers, WordSpec}
import org.thryft.native_.InternetDomainName

class DomainNameRegistrySpec extends WordSpec with Matchers {

  import DomainNameRegistry._

  "A date of epoch" should {
    "be ignored" in {
      ignoringEpoch(Some(new java.util.Date(0)), null) shouldBe None
    }
  }

  "Google optional" should {
    "map to Scala" in {
      toOption(Optional.of("x")) shouldBe Some("x")
      toOption(Optional.absent()) shouldBe None
    }
  }

  "An attempt to parse" should {
    val badDomain = InternetDomainName.from("x")
    val goodDNS   = "whois.networksolutions.com"
    "yield none for bad domains" in {
      attemptParse(badDomain, goodDNS) shouldBe None
    }
  }

  "Splitting URLs" should {
    val tld       = "co.uk"
    val hostname  = "bbc"
    "separate host from TLD" in {
      splitTLDs(s"$hostname.$tld", Set(tld)) shouldBe (hostname, tld)
    }
  }

  "The mappings between TLDs and DNS" should {
    val com   = "com"
    val coUk  = "co.uk"
    val dns   = Set("x." + com, "y." + com, "z." + coUk)
    val tlds  = Set(com, coUk)
    "provide DNSs grouped by TLD" in {
      val mappings = tldToDNS(dns, tlds)
      mappings should have size 2
      mappings(com) should have size 2
      mappings(coUk) should have size 1
    }
  }

}
