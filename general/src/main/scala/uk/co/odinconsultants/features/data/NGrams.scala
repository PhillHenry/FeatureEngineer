package uk.co.odinconsultants.features.data

object NGrams {

  type NGramFn = String => Seq[String]

  val Unigrams: NGramFn = NGrams.ngramsOf(1, _)
  val Bigrams:  NGramFn = NGrams.ngramsOf(2, _)
  val Trigrams: NGramFn = NGrams.ngramsOf(3, _)

  val NGram: Map[Int, NGramFn] = Map(1 -> Unigrams, 2 -> Bigrams, 3 -> Trigrams)

  def ngramsOf[T](n: Int, xs: Seq[T]): Seq[String] = xs.sliding(n).map(_.mkString("")).toSeq

  def counts(xs: Seq[String]): Map[String, Int] = {
    val x2i = collection.mutable.Map[String, Int]().withDefault(_ => 0)
    xs.foldLeft(x2i) { case (acc, x) =>
        x2i(x) = x2i(x) + 1
        x2i
    }
    x2i.toMap
  }

}
