package uk.co.odinconsultants.features.domain_ip.address

import java.io.{BufferedReader, InputStreamReader}
import java.net.InetAddress
import java.util.Date

import com.google.common.base.Optional
import org.thryft.native_.InternetDomainName

import scala.collection.mutable.ArrayBuffer
import scala.util.Try

object DomainNameRegistry {

  def main(args: Array[String]): Unit = {
    val xs = tlds()
    println(s"Number of TLDs: ${xs.length}")
    minorg(xs)
  }

  def apache(): Unit = {
    import org.apache.commons.net.whois.WhoisClient
    val client = new WhoisClient()
    println(client.query("robomarkets.com"))
    client.disconnect()
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

  def minorg(tlds: Seq[String]): Unit = {
    val domains = Seq(
//      "robomarkets.com",
//      "mx5.umu.se",
//      "mx5.qatarairways.com.qa",
//      "mx4.rcsecured.rcimx.net",
      "mx4.mk.de",
//      "mx4.hin.ch",
      "95a49f09385f5fb73aa3d1e994314a45b8d51f17.com"
    )

    domains.foreach { domain =>
      val (name, tld) = splitTLDs(domain, tlds)
      val lastDot     = name.lastIndexOf(".")
      val hostname    = if (lastDot == -1) name else name.substring(lastDot + 1)
      val cleaned     = s"$hostname$tld"
//      println(s"$hostname + $tld = $cleaned (name = $name, tld = $tld)")
      val optCreationDate = creationDateOf(cleaned) //
      println(s"$domain: $optCreationDate")
    }

  }

  def splitTLDs(domain: String, tlds: Seq[String]): (String, String) = {
    val orderedTLDs = longestToShortest(tlds.toSet)
    val tld         = orderedTLDs.find(x => domain.endsWith(s".$x"))
    tld match {
      case Some(t)  =>
        val splitPt = domain.length - t.length - 1
        (domain.substring(0, splitPt), domain.substring(splitPt))
      case None     => (domain, domain)
    }
  }

  def longestToShortest(xs: Set[String]): Seq[String] =
    xs.toList.sortBy(- _.length)

  private def creationDateOf(domain: String): Option[Date] = {
    println(s"domain = $domain")

    import org.thryft.native_.InternetDomainName
    val address         = InternetDomainName.from(domain)
    val whoIsServers    = Seq( // sampled from https://stackoverflow.com/questions/18270575/the-list-of-all-com-and-net-whois-servers
      "whois.godaddy.com",
      "whois.networksolutions.com",
      "whois.enom.com",
      "whois.name.com",
      "whois.tucows.com",
      "whois.PublicDomainRegistry.com",
      "whois.register.com",
      "whois.wildwestdomains.com",
      "whois.markmonitor.com",
      "whois2.softlayer.com",
      "whois2.domain.com")
    val answers         = whoIsServers.flatMap { x =>
      println(s"Querying: $x")
      val whoIsServer     = InetAddress.getByName(x)

      toOption(attemptParse(address, whoIsServer))
    }
    answers.headOption
  }

  def toOption[T](x: Optional[T]): Option[T] = if (x.isPresent) Some(x.get) else None

  def attemptParse(address: InternetDomainName, whoIsServer: InetAddress): Optional[java.util.Date] =
    Try {
      val parser = new io.github.minorg.whoisclient.WhoisClient()
      val record = parser.getWhoisRecord(address, whoIsServer)
      record.getParsed.getCreationDate
    }.getOrElse(Optional.absent())

}
