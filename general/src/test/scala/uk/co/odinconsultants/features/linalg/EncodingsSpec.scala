package uk.co.odinconsultants.features.linalg

import org.scalatest.{Matchers, WordSpec}

import scala.collection.immutable.IndexedSeq
import uk.co.odinconsultants.features.linalg.Space

class EncodingsSpec extends WordSpec with Matchers {

  import Encodings._

  val domain: IndexedSeq[Char]  = ('0' to '9')
  val space                     = Space[Char, Double](domain.toSet)
  val testString                = "123"

  "One hot encoding vectors" should {
    "be present for each element" in {
      to1HotVectors(testString, space) should have size testString.length
    }
    "be the same size as the domain" in {
      to1HotVectors(testString, space).foreach { vec =>
        vec should have length domain.length
      }
    }
  }

  "one hot encoding" should {
    "have size (n * |domain|)" in {
      to1HotEncoding(testString, space) should have size (testString.length * domain.length)
    }
  }

  "Population distribution" should {
    "have the same length as the features" in {
      toPopulationDistn(testString, space) should have length domain.length
    }
  }

}
