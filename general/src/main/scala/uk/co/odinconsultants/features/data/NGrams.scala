package uk.co.odinconsultants.features.data

object NGrams {

  val Unigrams: String => Seq[String] = NGrams.ngramsOf(1, _)

  def ngramsOf[T](n: Int, xs: Seq[T]): Seq[String] = xs.sliding(n).map(_.mkString("")).toSeq

}
