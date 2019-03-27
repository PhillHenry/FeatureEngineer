package uk.co.odinconsultants.features.stats

import org.scalatest.{Matchers, WordSpec}
import uk.co.odinconsultants.features.data.NGrams._

class EntropiesSpec extends WordSpec with Matchers {

  import Entropies._

  "Tossing a fair coin" should {
    val ps = Map("h" -> 0.5, "t" -> 0.5)
    "have entopy of 0.5" in {
      entropyOf("h", Unigrams, ps) shouldBe 0.5
    }
    "have additive entropy when tossed several times" in {
      entropyOf("hh", Unigrams, ps) shouldBe 1d
    }
    "ignore results that are neither head nor tails" in {
      entropyOf("hX", Unigrams, ps) shouldBe 0.5
    }
  }

}
