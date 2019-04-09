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
    val domains = Seq(
      "netflix.com",
      "api-global.netflix.com",
      "prod.netflix.com",
      "push.prod.netflix.com",
      "ichnaea.netflix.com",
      "google.com",
      "secure.netflix.com",
      "microsoft.com",
      "nrdp51-appboot.netflix.com",
      "www.google.com",
      "nflxso.net",
      "data.microsoft.com",
      "1.nflxso.net",
      "ftl.netflix.com",
      "facebook.com",
      "customerevents.netflix.com",
      "uiboot.netflix.com",
      "settings-win.data.microsoft.com",
      "prod.ftl.netflix.com",
      "nccp.netflix.com",
      "www.bbc.co.uk", // Note: registration date is in unexpected format
      "mx4.mk.de", // can't find a DNS for this anywhere
      "95a49f09385f5fb73aa3d1e994314a45b8d51f17.com" // first alphabetically ordered DNS to resolve is whois.aitdomains.com
    )
    Tld2DnsParser.readMappings.right.foreach { mappings =>
      val t2d           = sortByLongestTLD(mappings.toSeq)
      val tlds          = loadTLDs()
      val dates         = datesOf(domains, tlds, t2d, apacheWhois)
      val namesAndDates = domains.zip(dates)
      namesAndDates.foreach(println)
      println("# undefined: " + namesAndDates.count(_._2.isEmpty))
    }
  }

  def sortByLongestTLD(xs: Seq[TLD2Domain]): Seq[TLD2Domain] = xs.sortBy(- _._1.length)

  type TLD2Domain = (String, String)

  type WhoIsFn = (String, String) => Option[RecordData]

  def datesOf(domains:  Seq[String],
              tlds:     Seq[String],
              t2d:      Seq[TLD2Domain],
              fn:       WhoIsFn): Seq[Option[RecordData]] =
    domains.map { x =>
      val maybeDNS = suitableDNSFor(x, t2d)
      maybeDNS.flatMap { dns =>
        val cleaned = clean(tlds, x)
        fn(dns, cleaned)
      }
    }

  type DNS2Domains = (Option[String], Seq[String])

  def suitableDNSFor(domain: String, t2d: Seq[TLD2Domain]): Option[String] =
    t2d.view.find { case (t, _) =>
      domain.endsWith(t)
    }.map(_._2)

  def apacheWhois(dns: String, domain: String): Option[RecordData] = Try {
    val client  = whoIsConnection(dns)
    val str     = client.query(domain)
    val opt     = parse(dns, domain, str)
    client.disconnect()
    opt
  } match {
    case Success(x) => x
    case Failure(x) =>
      println(s"$dns failed with ${x.getMessage}")
      None
  }

  def parse(dns: String, x: String, str: String): Option[RecordData] = {
    val parser = new WhoisRecordParser()
    val record = RawWhoisRecord.create(InternetDomainName.from(x), ImmutableList.of(InternetDomainName.from(dns)), new Date, str)
    val parsed = parser.parse(record)
    toRecordData(parsed.getParsed)
  }

  private def whoIsConnection(dns: String): WhoisClient = {
    val client = new WhoisClient()
    client.setConnectTimeout(5000)
    client.setDefaultTimeout(5000)
    client.connect(dns)
    client
  }

  def loadTLDs(): Seq[String] = {
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
    val date = toOption(parsed.getCreationDate) match {
      case x @ Some(_)  => x
      case None         => toOption(parsed.getUpdatedDate)
    }
    ignoringEpoch(date, parsed)
  }

  def ignoringEpoch(date: Option[Date], parsed: ParsedWhoisRecord): Option[RecordData] = {
    date.flatMap { x =>
      if (x.getTime <= 3600L * 1000L * 25) None else  Some((x, toOption(parsed.getExpirationDate))) // Some DNS servers report 1 Jan 1970 in their timezone
    }
  }

}
