package uk.co.odinconsultants.features.domain_ip.address.name

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, WordSpec}

@RunWith(classOf[JUnitRunner])
class CleanerSpec extends WordSpec with Matchers {

  import Cleaner._

  "Non-ascii characters" should {
    "map to ascii" ignore new VodafoneFixture {
      similar foreach { xs =>
        toSimilarAscii(xs) foreach { x =>
          withClue(xs) {
            asciiUnicde should contain (x.toString)
          }
        }
      }
    }
  }

}
