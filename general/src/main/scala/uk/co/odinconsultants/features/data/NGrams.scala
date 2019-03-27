package uk.co.odinconsultants.features.data

object NGrams {

  type NGramFn = String => Seq[String]

  val CharUnigrams: NGramFn = _.split("")
  val Unigrams:     NGramFn = NGrams.ngramsOf(1, _)
  val Bigrams:      NGramFn = NGrams.ngramsOf(2, _)
  val Trigrams:     NGramFn = NGrams.ngramsOf(3, _)

  val NGram: Map[Int, NGramFn] = Map(1 -> Unigrams, 2 -> Bigrams, 3 -> Trigrams)

  def ngramsOf[T](n: Int, xs: Seq[T]): Seq[String] = xs.sliding(n).map(_.mkString("")).toSeq

}
