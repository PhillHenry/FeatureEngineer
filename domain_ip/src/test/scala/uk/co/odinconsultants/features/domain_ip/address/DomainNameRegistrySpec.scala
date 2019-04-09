package uk.co.odinconsultants.features.domain_ip.address

import java.util.Date

import com.google.common.base.Optional
import io.github.minorg.whoisclient.ParsedWhoisRecord
import org.junit.runner.RunWith
import org.mockito.Mockito._
import org.scalatest.junit.JUnitRunner
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{Matchers, WordSpec}
import org.thryft.native_.InternetDomainName

import scala.util.Random

@RunWith(classOf[JUnitRunner])
class DomainNameRegistrySpec extends WordSpec with Matchers with MockitoSugar {

  import DomainNameRegistry._

  "Calls to the client" should {
    "be grouped" in new DomainNameRegistryFixture {
      private val d2ns          = collection.mutable.Map[String, Seq[String]]()
      private val fn:   WhoIsFn = { case (d, xs) =>
        println(s"$d -> $xs")
        d2ns(d) = xs
        xs.map (_ => Some(mock[RecordData]))
      }

      private val dates = datesOf(domains, tlds, t2d, fn)

      dates should have size domains.size

      d2ns should have size 2
      d2ns(dnsCom) should have size 2
      d2ns(dnsUk) should have size 1
    }
  }

  "domains" should {
    "be grouped by DNS" in new DomainNameRegistryFixture{
      val dns2Domains: Seq[DNS2Domains] = dnsToDomains(domains, t2d)

      dns2Domains should have size 2

      def assertKV(k: Option[String], v: Seq[String]): Unit =
        dns2Domains.find(_._1 == k ).get shouldBe k -> v

      assertKV(Some(dnsCom), Seq(xCom, yCom))
      assertKV(Some(dnsUk), Seq(zCoUk))
    }
  }

  "An appropriate DNS" should {
    "be chosen for a domain name" in new DomainNameRegistryFixture {
      suitableDNSFor(xCom, t2d) shouldBe Some(dnsCom)
      suitableDNSFor(zCoUk, t2d) shouldBe Some(dnsUk)
    }
  }

  "TLD/DNS mappings" should {
    "be sorted by TLD length" in {
      val xs: Seq[TLD2Domain] = (1 to 10).map(x => ("x" * x, x.toString))
      sortByLongestTLD(Random.shuffle(xs)) shouldBe xs.reverse
    }
  }

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

}
