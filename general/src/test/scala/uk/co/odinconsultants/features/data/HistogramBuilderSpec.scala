package uk.co.odinconsultants.features.data

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, WordSpec}
import uk.co.odinconsultants.features.data.NGrams.Unigrams

@RunWith(classOf[JUnitRunner])
class HistogramBuilderSpec extends WordSpec with Matchers {

  import HistogramBuilder._

  "histogram of ngrams" should {
    "total correctly" in {
      val text      = "abcde"
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

}
