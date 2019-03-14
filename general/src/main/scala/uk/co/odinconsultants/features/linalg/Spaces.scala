package uk.co.odinconsultants.features.linalg

import scala.reflect.ClassTag

case class Space[T, U : Numeric : ClassTag](domain: IndexedSeq[T]) {

  val op: Numeric[U] = implicitly[Numeric[U]]

  val toIndex: Map[T, Int] = domain.zipWithIndex.toMap

  val zero: U = op.zero

}

object Spaces {

  type Index          = Int
  type Element        = Double
  type Vec[T]         = Array[T]

  def toVectors[T, U : Numeric : ClassTag](x: Seq[T], space: Space[T, U]): Seq[Vec[U]] = {

    def emptyVector: Vec[U] = Array.fill(space.domain.size)(space.zero)

    x.map { x =>
      val arr = emptyVector
      arr(space.toIndex(x)) = space.op.fromInt(1)
      arr
    }
  }

}
