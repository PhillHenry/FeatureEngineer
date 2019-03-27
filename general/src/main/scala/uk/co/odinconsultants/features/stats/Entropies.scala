package uk.co.odinconsultants.features.stats

import uk.co.odinconsultants.features.data.NGrams.NGramFn
import uk.co.odinconsultants.smaths.stats.ShannonEntropy

object Entropies {

  def entropyOf(nGramFn: NGramFn, x: String, ps: Map[String, Double]): Double =
    nGramFn(x).map(ps(_)).map(ShannonEntropy.entropyOf).sum

}
