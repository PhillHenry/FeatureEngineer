package uk.co.odinconsultants.features.domain_ip.address

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, WordSpec}

@RunWith(classOf[JUnitRunner])
class HttpCheckerIntegrationSpec extends WordSpec with Matchers {

  import HttpChecker._

  "BBC" should {
    "be HTTP 200" in {
      httpCodeCalling("https://bbc.co.uk/news") shouldBe 200
    }
  }

}
