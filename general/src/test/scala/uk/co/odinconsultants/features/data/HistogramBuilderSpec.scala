package uk.co.odinconsultants.features.data

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, WordSpec}
import uk.co.odinconsultants.features.data.NGrams.{NGramFn, Unigrams}

@RunWith(classOf[JUnitRunner])
class HistogramBuilderSpec extends WordSpec with Matchers {

  import HistogramBuilder._

  val text      = "abcde"
  val CharUnigrams: String => Seq[String] = _.split("")

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


    val extra       = text.head.toString
    val nonUniform  = text.split("") :+ extra
    "total nGram occurrences" in {
      val x2i         = histogramOf(nonUniform.toSeq, CharUnigrams)
      x2i should have size text.length
      x2i(extra) shouldBe 2
    }

    "Be parallelizable" in {
      val x2i         = histogramOf(nonUniform.toSeq.par, CharUnigrams)
      x2i should have size text.length
    }
  }

  "Probabilities" should {
    "sum to 1" in {
      val histo = histogramOf(Seq(text), Unigrams).map { case (k, v) => k.toString -> v.toInt }
      val ps    = toProbabilities(histo)
      ps.values.sum shouldBe 1d
      ps should have size text.length
    }
  }


}
