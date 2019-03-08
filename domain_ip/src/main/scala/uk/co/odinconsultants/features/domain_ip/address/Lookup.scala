package uk.co.odinconsultants.features.domain_ip.address

import com.maxmind.db.{CHMCache, Reader}

import scala.util.{Failure, Success, Try}

object Lookup {

  def readerOf(classpath: String): Reader = {
    val inputStream = this.getClass.getClassLoader.getResourceAsStream(classpath)
    new Reader(inputStream, new CHMCache())
  }

  def tryLookup(fn: => String): Either[Throwable, String] = {
    Try { fn } match {
      case Success(x) => Right(x)
      case Failure(x) => Left(x)
    }
  }

}
