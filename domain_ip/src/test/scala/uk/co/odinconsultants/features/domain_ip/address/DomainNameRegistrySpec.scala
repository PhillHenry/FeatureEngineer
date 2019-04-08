package uk.co.odinconsultants.features.domain_ip.address

import java.util.Date

import com.google.common.base.Optional
import io.github.minorg.whoisclient.ParsedWhoisRecord
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{Matchers, WordSpec}
import org.thryft.native_.InternetDomainName
import com.google.common.base.Optional

@RunWith(classOf[JUnitRunner])
class DomainNameRegistrySpec extends WordSpec with Matchers with MockitoSugar {

  import DomainNameRegistry._

  "A record" should {
    val date        = new Date()
    val anotherDate = new Date(date.getTime + 1)
    val noDate      = Optional.absent[Date]()
    "use the last updated date if creation date is unavailable" in {
      val mockRecord = mock[ParsedWhoisRecord]
      when(mockRecord.getCreationDate).thenReturn(noDate)
      when(mockRecord.getExpirationDate).thenReturn(noDate)
      when(mockRecord.getUpdatedDate).thenReturn(Optional.of(date))
      toRecordData(mockRecord) shouldBe Some(date, None)
    }
    "also use the expiration data if available" in {
      val mockRecord = mock[ParsedWhoisRecord]
      when(mockRecord.getCreationDate).thenReturn(Optional.of(date))
      when(mockRecord.getExpirationDate).thenReturn(Optional.of(anotherDate))
      toRecordData(mockRecord) shouldBe Some(date, Some(anotherDate))
    }
    "use the creation date even if expiration date is unavailable" in {
      val mockRecord = mock[ParsedWhoisRecord]
      when(mockRecord.getCreationDate).thenReturn(Optional.of(date))
      when(mockRecord.getExpirationDate).thenReturn(noDate)
      toRecordData(mockRecord) shouldBe Some(date, None)
    }
  }

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
