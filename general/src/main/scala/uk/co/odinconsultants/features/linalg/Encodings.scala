package uk.co.odinconsultants.features.linalg

import uk.co.odinconsultants.features.linalg.Spaces.{Vec, toVectors}

import scala.reflect.ClassTag

object Encodings {

  def to1HotEncoding[T, U : Numeric : ClassTag](x: Seq[T], space: Space[T, U]): Vec[U] = {
    val charVecs = toVectors(x, space)
    charVecs.foldLeft(Array.fill(0)(space.zero)) { case (acc, xs) => acc ++ xs}
  }

}
