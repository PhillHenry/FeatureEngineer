package uk.co.odinconsultants.features.linalg

import uk.co.odinconsultants.features.linalg.Spaces.Vec

import scala.reflect.ClassTag

object Encodings {

  type EncodingFn[T] = (Vec[T], Vec[T]) => Vec[T]

  def to1HotEncoding[T, U : Numeric : ClassTag](toEncode: Seq[T], space: Space[T, U]): Vec[U] = {
    val appending: EncodingFn[U] = (acc, xs) => acc ++ xs
    encode(toEncode, space, appending)
  }

  def encode[U: Numeric : ClassTag, T](x: Seq[T], space: Space[T, U], encoding: EncodingFn[U]): Vec[U] = {
    val charVecs  = to1HotVectors(x, space)
    val seed      = Array.fill(0)(space.zero)
    charVecs.foldLeft(seed)(encoding)
  }

  def to1HotVectors[T, U : Numeric : ClassTag](x: Seq[T], space: Space[T, U]): Seq[Vec[U]] = {

    def emptyVector: Vec[U] = Array.fill(space.domain.size)(space.zero)

    x.map { x =>
      val arr = emptyVector
      arr(space.toIndex(x)) = space.op.fromInt(1)
      arr
    }
  }

}
