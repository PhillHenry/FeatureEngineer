package uk.co.odinconsultants.features.domain_ip.address

import java.net.InetAddress

object DomainNameRegistry {

  def main(args: Array[String]): Unit = {
    // TEST:
    minorg()
  }

  def apache(): Unit = {
    import org.apache.commons.net.whois.WhoisClient
    val client = new WhoisClient()
    //    client.connect(WhoisClient.DEFAULT_HOST) // which is "whois.internic.net"
//    client.connect("whois.godaddy.com") // this works :)
    println(client.query("robomarkets.com"))
    //    println(client.query("bbc.co.uk"))
    client.disconnect()
  }

  def minorg(): Unit = {
    import io.github.minorg.whoisclient.WhoisClient
    import org.thryft.native_.InternetDomainName
    val address = InternetDomainName.from("robomarkets.com")
//    val whoisServer = InetAddress.getByName("whois.godaddy.com") // this works
    val whoisServer = InetAddress.getByName("whois.verisign-grs.com") // but so does this
    val parser = new WhoisClient()
    val record = parser.getWhoisRecord(address, whoisServer)
    println(record.getParsed.getCreationDate)
  }

}
