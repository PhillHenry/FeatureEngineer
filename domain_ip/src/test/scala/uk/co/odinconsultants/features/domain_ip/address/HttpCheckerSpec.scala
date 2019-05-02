package uk.co.odinconsultants.features.domain_ip.address

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, WordSpec}

@RunWith(classOf[JUnitRunner])
class HttpCheckerSpec extends WordSpec with Matchers {

  import HttpChecker._

  val host = "bbc.co.uk"

  "A URL" should {
    s"have $protocol added if it has none" in {
      clean(host) shouldBe s"${protocol}$host"
    }
    s"be left alone if it begins with $protocol" in {
      clean(s"${protocol}$host") shouldBe s"${protocol}$host"
    }
    s"be upgraded to HTTPS if it's only HTTP" in {
      clean(s"http://$host") shouldBe s"${protocol}$host"
    }
  }

}
