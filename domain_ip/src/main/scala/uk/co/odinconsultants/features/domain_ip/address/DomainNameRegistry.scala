package uk.co.odinconsultants.features.domain_ip.address

import java.io.{BufferedReader, InputStreamReader}
import java.net.InetAddress
import java.util
import java.util.{ArrayList, Date, List}

import com.google.common.base.Optional
import com.google.common.collect.ImmutableList
import io.github.minorg.whoisclient.{ParsedWhoisRecord, RawWhoisRecord}
import io.github.minorg.whoisclient.parser.WhoisRecordParser
import org.apache.commons.net.whois.WhoisClient
import org.thryft.native_.InternetDomainName

import scala.collection.mutable.ArrayBuffer
import scala.util.{Failure, Success, Try}

object DomainNameRegistry {

  def log(x: String): Unit = println(s"${new Date}: $x")

  def main(args: Array[String]): Unit = {
    val xs = tlds()
    val domains = Seq(
      "www.bbc.co.uk",
      "mx4.mk.de",
      "95a49f09385f5fb73aa3d1e994314a45b8d51f17.com" // first alphabetically ordered DNS to resolve is whois.aitdomains.com
    )
    Tld2DnsParser.readMappings.right.foreach { mappings =>
      val t2d = mappings.toSeq.sortBy(- _._1.length)
      val dates = domains.map { x =>
        t2d.view.filter { case (t, _) =>
          x.endsWith(t)
        }.headOption.map{ case (_, dns) =>
          val cleaned = clean(tlds, x)
          apacheWhois(dns, cleaned)
        }
      }
      dates.foreach(println)
    }
  }

  def apacheWhois(dns: String, x: String): Option[RecordData] = Try {
    val client = new WhoisClient()
    client.setConnectTimeout(5000)
    client.setDefaultTimeout(5000)
    client.connect(dns)
    val str = client.query(x)
    log(str)
    val parser = new WhoisRecordParser()
    val record = RawWhoisRecord.create(InternetDomainName.from(x), ImmutableList.of(InternetDomainName.from(dns)), new Date, str)
    val parsed = parser.parse(record)
    val opt = toRecordData(parsed.getParsed)
    client.disconnect()
    opt
  } match {
    case Success(x) => x
    case Failure(x) =>
      println(s"$dns failed with ${x.getMessage}")
      None
  }

  def tlds(): Seq[String] = {
    val stream  = DomainNameRegistry.getClass.getClassLoader.getResourceAsStream("top-1m-TLD.csv")
    val buffer  = new BufferedReader(new InputStreamReader(stream))
    val output  = new ArrayBuffer[String]()
    var line    = buffer.readLine()
    while (line != null) {
      output += line.substring(line.indexOf(",") + 1)
      line    = buffer.readLine()
    }
    output
  }

  def clean(tlds: Seq[String], domain: String): String = {
    val (name, tld) = splitTLDs(domain, tlds.toSet)
    val lastDot = name.lastIndexOf(".")
    val hostname = if (lastDot == -1) name else name.substring(lastDot + 1)
    s"$hostname.$tld"
  }

  def splitTLDs(domain: String, tlds: Set[String]): (String, String) = {
    val orderedTLDs = longestToShortest(tlds)
    val tld         = orderedTLDs.find(x => domain.endsWith(s".$x"))
    tld match {
      case Some(t)  =>
        val splitPt = domain.length - t.length - 1
        (domain.substring(0, splitPt), domain.substring(splitPt + 1))
      case None     => (domain, domain)
    }
  }

  def longestToShortest(xs: Set[String]): Seq[String] = xs.toList.sortBy(- _.length)

  type RecordData = (Date, Option[Date])

  def firstMatch(domain: String, dns: Set[String]): Option[RecordData] = {
    import org.thryft.native_.InternetDomainName
    val address = InternetDomainName.from(domain)
    val answers = dns.map(_.toLowerCase).toList.sorted.zipWithIndex.view.flatMap { case (x, i) =>
      log(s"$i. Querying: $x about $address")
      attemptParse(address, x)
    }
    log(s"About to filter over ${dns.size} DNSs...")
    answers.headOption
  }

  def toOption[T](x: Optional[T]): Option[T] = if (x.isPresent) Some(x.get) else None

  def attemptParse(address: InternetDomainName, dnsName: String): Option[RecordData] = {
    val result = Try {
      val dns     = InetAddress.getByName(dnsName)
      val parser  = new io.github.minorg.whoisclient.WhoisClient()
      val record  = parser.getWhoisRecord(address, dns)
      val parsed  = record.getParsed
      toRecordData(parsed)
    }
    result match {
      case Success(x) => x
      case Failure(x) =>
        log(x.getClass.getSimpleName + ": " + x.getMessage)
        None
    }
  }

  def toRecordData(parsed: ParsedWhoisRecord): Option[RecordData] = {
    val date = toOption(parsed.getCreationDate)
    ignoringEpoch(date, parsed)
  }

  def ignoringEpoch(date: Option[Date], parsed: ParsedWhoisRecord): Option[RecordData] = {
    date.flatMap { x =>
      if (x.getTime <= 3600L * 1000L * 25) None else  Some((x, toOption(parsed.getExpirationDate))) // Some DNS servers report 1 Jan 1970 in their timezone
    }
  }

  def tldToDNS(dns: Set[String], tlds: Set[String]): Map[String, Set[String]] = {
    val tld2Dns = Map[String, Set[String]]().withDefault(_ => Set[String]())
    dns.foldLeft(tld2Dns) { case( t2d, x) =>
      val (_, tld)            = splitTLDs(x, tlds)
      val old:    Set[String] = t2d(tld)
      val added:  Set[String] = old + x
      t2d + (tld -> added)
    }
  }
}
