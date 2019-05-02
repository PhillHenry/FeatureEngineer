package uk.co.odinconsultants.features.domain_ip.address

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, WordSpec}

@RunWith(classOf[JUnitRunner])
class HttpCheckerIntegrationSpec extends WordSpec with Matchers {

  import HttpChecker._

  "BBC" should {
    val bbc = "bbc.co.uk"
    "be HTTP 200" in {
      httpsCodeOf("https://" + bbc + "/") shouldBe 200
    }
    "Redirect you to a secure connection " in {
      httpCodeOf(bbc, 80) shouldBe 301
    }
  }

}
