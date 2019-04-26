package uk.co.odinconsultants.features.domain_ip.address

object Addresses {

  def removePorts(x: String): String = {
    val reg = """(.+):\d+""".r
    x match {
      case reg(y) => y
      case _  => x
    }
  }

  def isIPAddress(x: String): Boolean = {
    val reg = """.*(\d{1,3})\.(\d{1,3})\.(\d{1,3})\.(\d{1,3}).*""".r
    x match {
      case reg(_, _, _, _) => true
      case _ => false
    }
  }

  def removeTLD(orderedTLDs: Seq[String], domain: String): String = {
    val tld = orderedTLDs.find(x => domain.endsWith(s".$x"))
    tld match {
      case Some(t)  =>
        val withoutTLD  = domain.substring(0, domain.length - t.length - 1)
        val lastDot     = withoutTLD.lastIndexOf(".")
        val hostname    = if (lastDot == -1) withoutTLD else withoutTLD.substring(lastDot + 1)
        hostname
      case None     => domain
    }
  }

  def longestToShortest(xs: Set[String]): Seq[String] =
    xs.toList.sortBy(- _.length)
}
