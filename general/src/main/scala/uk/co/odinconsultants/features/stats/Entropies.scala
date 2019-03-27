package uk.co.odinconsultants.features.stats

import uk.co.odinconsultants.features.data.NGrams.NGramFn
import uk.co.odinconsultants.smaths.stats.ShannonEntropy

object Entropies {

  def entropyOf(x: String, nGramFn: NGramFn, ps: Map[String, Double]): Double =
    nGramFn(x).flatMap(ps.get(_)).map(ShannonEntropy.entropyOf).sum

}
