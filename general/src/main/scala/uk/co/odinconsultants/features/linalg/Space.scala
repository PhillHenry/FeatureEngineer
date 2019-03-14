package uk.co.odinconsultants.features.linalg

import scala.reflect.ClassTag

case class Space[T, U : Numeric : ClassTag](domain: IndexedSeq[T]) {

  val op: Numeric[U] = implicitly[Numeric[U]]

  val toIndex: Map[T, Int] = domain.zipWithIndex.toMap

  val zero: U = op.zero

}
