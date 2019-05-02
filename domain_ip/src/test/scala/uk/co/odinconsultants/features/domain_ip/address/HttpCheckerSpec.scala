package uk.co.odinconsultants.features.domain_ip.address

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, WordSpec}

@RunWith(classOf[JUnitRunner])
class HttpCheckerSpec extends WordSpec with Matchers {

  import HttpChecker._

  val host = "bbc.co.uk"

  "A URL" should {
    s"have $https added if it has none" in {
      ensureHttps(host) shouldBe s"${https}$host"
    }
    s"be left alone if it begins with $https" in {
      ensureHttps(s"${https}$host") shouldBe s"${https}$host"
    }
    s"be upgraded to HTTPS if it's only HTTP" in {
      ensureHttps(s"http://$host") shouldBe s"${https}$host"
    }
    "be http only if the port is 80" in {
      clean(host, 80) shouldBe s"${http}$host"
    }
  }

}
