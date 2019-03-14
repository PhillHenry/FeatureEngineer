package uk.co.odinconsultants.features.linalg

import uk.co.odinconsultants.features.linalg.Spaces.Vec

import scala.reflect.ClassTag

object Encodings {

  type EncodingFn[T] = (Vec[T], Vec[T]) => Vec[T]

  def toPopulationDistn[T, U : Numeric : ClassTag](xs: Seq[T], space: Space[T, U]): Vec[U] = {
    val adding: EncodingFn[U] = (acc, xs) => acc.zip(xs).map { case (x, y) => space.op.plus(x, y) }
    val seed                      = Array.fill(space.domain.size)(space.zero)
    encode(xs, space, adding, seed)
  }

  def to1HotEncoding[T, U : Numeric : ClassTag](xs: Seq[T], space: Space[T, U]): Vec[U] = {
    val appending: EncodingFn[U]  = (acc, xs) => acc ++ xs
    val seed                      = Array.fill(0)(space.zero)
    encode(xs, space, appending, seed)
  }

  def encode[U: Numeric : ClassTag, T](xs: Seq[T], space: Space[T, U], encoding: EncodingFn[U], seed: Vec[U]): Vec[U] =
    to1HotVectors(xs, space).foldLeft(seed)(encoding)

  def to1HotVectors[T, U : Numeric : ClassTag](xs: Seq[T], space: Space[T, U]): Seq[Vec[U]] = {

    def emptyVector: Vec[U] = Array.fill(space.domain.size)(space.zero)

    xs.map { x =>
      val arr = emptyVector
      arr(space.toIndex(x)) = space.op.fromInt(1)
      arr
    }
  }

}
