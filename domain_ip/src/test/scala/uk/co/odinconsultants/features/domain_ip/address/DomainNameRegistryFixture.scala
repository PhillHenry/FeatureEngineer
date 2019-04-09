package uk.co.odinconsultants.features.domain_ip.address

trait DomainNameRegistryFixture {
  val com:      String                = "com"
  val coUk:     String                = "co.uk"
  val xCom:     String                = "x." + com
  val yCom:     String                = "y." + com
  val zCoUk:    String                = "z." + coUk
  val domains:  Seq[String]           = Seq(xCom, yCom, zCoUk)
  val tlds:     Seq[String]           = Seq(com, coUk)
  val dnsCom:   String                = "whois.verisign-grs.com"
  val dnsUk:    String                = "whois.nic.uk"
  val t2d:      Seq[(String, String)] = Seq(com -> dnsCom, coUk -> dnsUk)
}
