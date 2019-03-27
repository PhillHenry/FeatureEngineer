package uk.co.odinconsultants.features.data

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, WordSpec}

@RunWith(classOf[JUnitRunner])
class NGramsSpec extends WordSpec with Matchers {

  import NGrams._

  val seq = "1234567890"

  s"1 grams of $seq" should {
    "have size n-1" ignore {
      ngramsOf(2, seq) should have size (seq.length - 1)
    }
  }
  s"1 grams of $seq" should {
    "have each element" in {
      ngramsOf(1, seq).mkString("") shouldBe seq
      ngramsOf(1, seq) shouldBe seq.split("").map(_.toCharArray.head)
    }
  }

}
