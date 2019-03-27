package uk.co.odinconsultants.features.cat

trait Monads[T] {
  def op[T](xs: Seq[T]): T
}

object Monads {

  implicit val StringMonad = new Monads[String] {
    override def op[T](xs: Seq[T]): T = xs.mkString("").asInstanceOf[T]
  }

}
