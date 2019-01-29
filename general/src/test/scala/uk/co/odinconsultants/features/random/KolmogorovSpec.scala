package uk.co.odinconsultants.features.random

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, WordSpec}

@RunWith(classOf[JUnitRunner])
class KolmogorovSpec extends WordSpec with Matchers {

  import Kolmogorov._

  val aChristmasCarol = "Oh! but he was a tight-fisted hand at the grindstone, Scrooge! a squeezing, wrenching, grasping, scraping, clutching, covetous, old sinner! Hard and sharp as flint, from which no steel had ever struck out generous fire; secret, and self-contained, and solitary as an oyster. The cold within him froze his old features, nipped his pointed nose, shrivelled his cheek, stiffened his gait; made his eyes red, his thin lips blue; and spoke out shrewdly in his grating voice. A frosty rime was on his head, and on his eyebrows, and his wiry chin. He carried his own low temperature always about with him; he iced his office in the dog-days; and didn't thaw it one degree at Christmas."

  "Input text" should {
    "be long than compressed text" in {
      score(aChristmasCarol) shouldBe < (aChristmasCarol.length)
    }
  }

  "Random text" should {
    val random = scala.util.Random.shuffle(aChristmasCarol.toList).mkString("")
    "be distinguishable from non-random" in {
      score(random) shouldBe > (score(aChristmasCarol))
    }
  }

}
