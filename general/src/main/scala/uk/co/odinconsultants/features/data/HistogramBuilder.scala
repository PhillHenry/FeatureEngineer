package uk.co.odinconsultants.features.data

import scala.collection.GenIterable

object HistogramBuilder {

  def histogramOf[T](xs: GenIterable[T], fn: T => Seq[T]): Map[T, Long] = {
    val t2Count = collection.mutable.Map[T, Long]().withDefault(_ => 0L)

    xs.foldLeft(t2Count) { case (acc, x) =>
      val xs = fn(x)
      xs.foreach { x =>
        val old = acc(x)
        acc(x) = (old + 1)
      }
      acc
    }.toMap
  }

}
