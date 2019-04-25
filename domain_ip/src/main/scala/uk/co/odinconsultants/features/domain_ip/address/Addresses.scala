package uk.co.odinconsultants.features.domain_ip.address

object Addresses {

  def isIPAddress(x: String): Boolean = {
    val reg = """.*(\d{1,3})\.(\d{1,3})\.(\d{1,3})\.(\d{1,3}).*""".r
    x match {
      case reg(_, _, _, _) => true
      case _ => false
    }
  }

}
