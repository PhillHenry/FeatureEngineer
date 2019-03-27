package uk.co.odinconsultants.features.data

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, WordSpec}

@RunWith(classOf[JUnitRunner])
class NGramsSpec extends WordSpec with Matchers {

  import NGrams._

  val seq = "12345"

  "Counts" should {
    "total nGram occurrences" in {
      val extra       = seq.head.toString
      val nonUniform  = seq.split("") :+ extra
      val x2i = counts(nonUniform.toSeq)
      x2i should have size seq.length
      x2i(extra) shouldBe 2
    }
  }

  s"bigrams of $seq" should {
    "have size n-1" in {
      ngramsOf(2, seq) should have size (seq.length - 1)
    }
    "contain all combinations" in {
      ngramsOf(2, "123") should contain ("12")
      ngramsOf(2, "123") should contain ("23")
      ngramsOf(2, "123") should have size 2
    }
  }
  s"unigrams of $seq" should {
    "have each element" in {
      ngramsOf(1, seq).mkString("") shouldBe seq
      ngramsOf(1, seq) shouldBe seq.split("")
    }
  }

}
