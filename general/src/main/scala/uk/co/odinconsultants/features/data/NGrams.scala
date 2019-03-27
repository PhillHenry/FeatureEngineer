package uk.co.odinconsultants.features.data

import uk.co.odinconsultants.features.cat.Monads

object NGrams {

//  def ngramsOf[T](n: Int, xs: Seq[T])(implicit t: Monads[T]): Seq[T] = xs.sliding(1).map(t.op(_)).toSeq

  def ngramsOf[T](n: Int, xs: Seq[T]): Seq[T] = xs.sliding(n).flatten.toSeq

}
