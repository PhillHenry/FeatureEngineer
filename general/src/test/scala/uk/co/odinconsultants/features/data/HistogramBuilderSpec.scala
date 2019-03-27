package uk.co.odinconsultants.features.data

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, WordSpec}
import uk.co.odinconsultants.features.data.NGrams.Unigrams

@RunWith(classOf[JUnitRunner])
class HistogramBuilderSpec extends WordSpec with Matchers {

  import HistogramBuilder._

  val text      = "abcde"

  "histogram of ngrams" should {
    "total correctly" in {
      val corpus    = Seq(text, text, text)

      val histogram = histogramOf(corpus, Unigrams)

      histogram should have size text.length
      text.foreach(x =>
        withClue(s"histogram:\n${histogram.toSeq.sorted.mkString("\n")}") {
          histogram(x.toString) shouldBe 3L
        }
      )
    }
  }

  "Counts" should {
    val fn: String => Seq[String] = _.split("")
    "total nGram occurrences" in {
      val extra       = text.head.toString
      val nonUniform  = text.split("") :+ extra
      val x2i         = histogramOf(nonUniform.toSeq, fn)
      x2i should have size text.length
      x2i(extra) shouldBe 2
    }
  }


}
