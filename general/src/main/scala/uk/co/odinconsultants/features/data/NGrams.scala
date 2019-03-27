package uk.co.odinconsultants.features.data

object NGrams {

  def ngramsOf[T](n: Int, xs: Seq[T]): Seq[String] = xs.sliding(n).map(_.mkString("")).toSeq

}
