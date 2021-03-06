package uk.co.odinconsultants.features.spark

import org.apache.spark.sql.DataFrame
import uk.co.odinconsultants.features.data.NGrams
import org.apache.spark.sql.functions.sum

object NGramMaker {

  def toHistogram(words: DataFrame, n: Int): DataFrame = {
    import words.sparkSession.implicits._
    val nGramFn = NGrams.NGram(n)
    val ngrams = words.rdd.flatMap(r => nGramFn(r.getString(0))).cache()
    ngrams.map(_ -> 1).reduceByKey(_ + _).toDF("ngram", "count")
  }

  def toCounts(counts: DataFrame): Double = {
    import counts.sparkSession.implicits._
    counts.agg(sum('count)).collect()(0).getLong(0).toDouble
  }

  def toProbabilities(df: DataFrame, n: Int): Map[String, Double] = {
    import df.sparkSession.implicits._
    val histo = toHistogram(df, n)
    val total = toCounts(histo)
    histo.map(r => r.getString(0) -> (r.getInt(1) / total)).collect().toMap
  }


}
