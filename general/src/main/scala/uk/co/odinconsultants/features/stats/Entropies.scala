package uk.co.odinconsultants.features.stats

import uk.co.odinconsultants.features.data.NGrams.NGramFn
import uk.co.odinconsultants.smaths.stats.ShannonEntropy

object Entropies {

  def entropyOf(x: String, nGramFn: NGramFn, ps: Map[String, Double]): Double =
    nGramFn(x).flatMap(ps.get(_)).map(ShannonEntropy.entropyOf).sum

  def entropyWithPenalty(x: String, nGramFn: NGramFn, ps: Map[String, Double], penalty: Double): Double = {
    def zeroOrEntropy(p: Double): Double = if (p ==0) 0d else ShannonEntropy.entropyOf(p)
    nGramFn(x).map(x => ps.getOrElse(x, 0d)).map(x => zeroOrEntropy(x) + penalty).sum
  }

}
