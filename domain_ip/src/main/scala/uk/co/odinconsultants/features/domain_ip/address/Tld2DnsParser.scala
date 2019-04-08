package uk.co.odinconsultants.features.domain_ip.address

import io.circe.parser._
import io.circe.{Decoder, Error}

import scala.io.Source

object Tld2DnsParser {

  def main(args: Array[String]): Unit = {
    val mappings = readMappings
    println(mappings)
  }

  def readMappings: Either[Error, Mapping] = {
    val is = Tld2DnsParser.getClass.getResourceAsStream("/tld2dns.json")
    val json = Source.fromInputStream(is).getLines().mkString("")
    parse(json)
  }

  type Mapping = Map[String, String]

  val decodeDetailParam: Decoder[Mapping] = Decoder.instance { x => // TopCursor
    val mappings = x.keys.getOrElse(List()).flatMap { k =>
      x.downField(k).get[String]("host") match {
        case Right(b) => Some(k -> b)
        case _        => None
      }
    }.toMap

    Right(mappings)
  }

  def parse(json: String): Either[Error, Mapping] =
    decode[Map[String, String]](json)(decodeDetailParam)

}
