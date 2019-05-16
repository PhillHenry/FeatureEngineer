package uk.co.odinconsultants.features.domain_ip.address.name

import uk.co.odinconsultants.features.data.HistogramBuilder._
import uk.co.odinconsultants.features.data.NGrams._
import uk.co.odinconsultants.smaths.stats.Divergence._

object Comparison extends PunyCode {

  type Probabilities      = Map[String, Double]
  type ProbabilitiesTuple = (Seq[Double], Seq[Double])

  def probabilitiesWBlanks(ns: Set[Int], x: String): Probabilities = {
    val to2GramWBlank: NGramFn = ngramsOf(3, _).map(x => x.head + "-" + x.last)
    probabilitiesOf(nGramFns(ns) + to2GramWBlank, x)
  }

  def nGramFns(ns: Set[Int]): Set[NGramFn] = {
    def toNGramFn(n: Int): NGramFn  = ngramsOf(n, _)
    ns.map(toNGramFn)
  }

  def probabilitiesOf(fns: Set[NGramFn], x: String): Probabilities = {
    val ngram2Histo: NGramFn => Map[String, Int] = { fn =>
      histogramOf(Seq(x), fn).map { case (k, v) => k -> v.toInt}
    }
    val histo = fns.flatMap(ngram2Histo).toMap
    toProbabilities(histo)
  }

  val correction = 1e-8
  val Comparison: CompareFn =  { case (x, y) => kl(x + correction, y + correction) }

  def clean(x: String): String = {
    val ascii = if (x.startsWith("xn--")) java.net.IDN.toUnicode(x) else x
    println(s"x = $x, ascii = $ascii")
    ascii.replaceAll(A, "a").replaceAll(O, "o").replaceAll(F, "f").replaceAll(E, "e").toLowerCase
  }

  def compare(baseline: Probabilities, x: String, ns: Set[Int]): Double = {
    val domain    = clean(x)
    val ps        = probabilitiesWBlanks(ns, domain)
    val (xs, ys)  = maps2Arrays(baseline, ps)
    total(Comparison, xs.toArray, ys.toArray) + math.abs(ps.size - baseline.size)
  }

  def maps2Arrays(ps: Probabilities, qs: Probabilities): ProbabilitiesTuple = {
    val pSet          = ps.keys.toSet
    val qSet          = qs.keys.toSet
    val intersection  = pSet.intersect(qSet)
    val common        = intersection.toList.map(k => (ps(k), qs(k)))
    val pDisjoint     = pSet.diff(qSet).toList.map(k => (ps(k), 0d))
    val qDisjoint     = qSet.diff(pSet).toList.map(k => (0d, qs(k)))

    (common ++ pDisjoint ++ qDisjoint).unzip
  }
}
