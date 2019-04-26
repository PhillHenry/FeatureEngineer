package uk.co.odinconsultants.features.spark

import org.apache.spark.sql.DataFrame
import uk.co.odinconsultants.features.data.NGrams
import org.apache.spark.sql.functions.sum

object NGramMaker {

  def toHistogram(words: DataFrame, n: Int): DataFrame = {
    import words.sparkSession.implicits._
    val nGramFn = NGrams.NGram(n)
    val ngrams = words.rdd.flatMap(r => nGramFn(r.getString(0))).cache()
    val counts = ngrams.map(_ -> 1).reduceByKey(_ + _).toDF("ngram", "count")
    counts
  }

  def toProbabilities(counts: DataFrame): Double = {
    import counts.sparkSession.implicits._
    val total     = counts.agg(sum('count)).collect()(0).getLong(0).toDouble
    total
  }

}