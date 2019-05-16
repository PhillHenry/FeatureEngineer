package uk.co.odinconsultants.features.domain_ip.address.name

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, WordSpec}

@RunWith(classOf[JUnitRunner])
class ComparisonSpec extends WordSpec with Matchers with VodafoneFixture {

  import Comparison._

  def numDistinctNGrams(n: Int, x: String): Int = x.sliding(n).toSet.size

  "Probabilities" should {
    def totalProbabilityFor(x: String, ns: Set[Int]): Double = probabilitiesOf(nGramFns(ns), x).values.sum

    val x = "1234567890"
    "add to 1.0" in {
      totalProbabilityFor(x, Set(1)) shouldBe 1.0 +- 0.0001
      totalProbabilityFor(x, Set(2)) shouldBe 1.0 +- 0.0001
      totalProbabilityFor(x, Set(1, 2)) shouldBe 1.0 +- 0.0001
    }
    "have a cardinality equal to number of distinct unigrams" in {
      probabilitiesOf(nGramFns(Set(1)), x) should have size x.length
    }
    "have a cardinality equal to number of distinct bigrams" in {
      probabilitiesOf(nGramFns(Set(2)), x) should have size (x.length - 1)
    }
    "have a cardinality equal to number of distinct bigrams and unigrams" in {
      probabilitiesOf(nGramFns(Set(1, 2)), x) should have size (x.length + (x.length - 1))
    }
    "have the same cardinality irrespective of order" in {
      probabilitiesOf(nGramFns(ns), x).size shouldBe probabilitiesOf(nGramFns(ns), x.reverse).size
    }
    "have a similar cardinality for similar strings" in {
      (probabilitiesOf(nGramFns(ns), x + "a").size - probabilitiesOf(nGramFns(ns), x).size) shouldBe ns.size
    }
  }

  "Probability maps" should {
    val ps = Map("a" -> 0.1, "b" -> 0.9)
    val qs = Map("z" -> 0.2, "b" -> 0.7, "c" -> 0.1)
    "be rendered as arrays" in {
      val (xs, ys) = maps2Arrays(ps, qs)
      withClue(s"xs = $xs, ys = $ys") {
        xs.sum shouldBe 1.0 +- 0.000001
        ys.sum shouldBe 1.0 +- 0.000001
        xs should have size ys.size
        xs should have size (ps.keys ++ qs.keys).toSet.size
      }
    }
    "ignore order" in {
      val (xs, ys) = maps2Arrays(ps, qs)
      val (as, bs) = maps2Arrays(qs, ps)

      xs.zip(ys).toSet shouldBe as.zip(bs).map(_.swap).toSet
    }

    def toPMap(xs: Seq[Any]): Probabilities = xs.map(x => x.toString -> 1d/xs.length).toMap

    "have no overlap if different keys" in {
      val (xs, ys)  = maps2Arrays(toPMap('a' to 'j'), toPMap('k' to 't'))
      val zipped    = xs.zip(ys)
      zipped.length should be > 0
      zipped.foreach { case (x, y) =>
          if (x != 0)
            y shouldBe 0d
          else
            y should be > 0d
      }
    }
    "have a lot of overlap" in {
      val a2j = toPMap('a' to 'j')
      val b2k = toPMap('b' to 'k')
      val (xs, ys)  = maps2Arrays(a2j, b2k)
      val zipped    = xs.zip(ys)
      withClue(s"\na2j = $a2j\nb2k = $b2k\nxs = $xs\nys = $ys\n") {
        zipped should have length 11
        zipped.filter { case (x, y) => x != 0d && y != 0d } should have length 9
      }
    }
  }

  "Probability vectors" should {
    val v0dafone = "v0dafone"

    "not be orthogonal for similar names" in {
      val ps        = probabilities(v0dafone)
      val (xs, ys)  = maps2Arrays(baseline, ps)
      xs.zip(ys).count { case (x, y) => x > 0 && y > 0 } should be > 0
    }
  }

  "Scores" should {

    println(s"Dissimilar:\n${asString(notSimilar)}")
    println(s"Similar:\n${asString(similar)}")

    val threshold = notSimilar.map(x => x -> compare(baseline, x, ns)).toList.sortBy(_._2).map(_._2).min

    s"icpavone should have a higher score than $vOdAFOnE" in { // ignored because vOdAFOnE really does have fewer ngrams than icpavone
      val ps_vOdAFOnE = probabilities(vOdAFOnE)
      val ps_icpavone = probabilities("icpavone")

      val voda = ps_vOdAFOnE.keySet.intersect(baseline.keySet)
      val rand = ps_icpavone.keySet.intersect(baseline.keySet)

      println(s"\nvoda = $voda, icpavone = $rand")

      withClue(s"icpavone = $rand vOdAFOnE = $voda\nvodafone = ${baseline.keySet}\nvOdAFOnE = ${ps_vOdAFOnE.keySet}\ncleaned = ${Cleaner.clean(vOdAFOnE)}\n") {
        voda.size should be > rand.size
      }
    }

    "zero when compared to self" in {
      compare(baseline, vodafone, ns) shouldBe 0d
    }

    s"$similar should all be better than ${notSimilar.mkString(", ")}" in {
      similar.foreach { x =>
        withClue(x) {
          compare(baseline, x, ns) should be < threshold
        }
      }
    }
  }

}
