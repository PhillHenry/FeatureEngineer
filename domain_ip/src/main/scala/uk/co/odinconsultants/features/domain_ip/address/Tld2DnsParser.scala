package uk.co.odinconsultants.features.domain_ip.address

import io.circe.Decoder.Result

import scala.io.Source
import io.circe.{Decoder, Error, Json, ParsingFailure}
import io.circe._
import io.circe.cursor.TopCursor
import io.circe.generic.semiauto._
import io.circe.parser._


object Tld2DnsParser {


  def main(args: Array[String]): Unit = {
    val is = Tld2DnsParser.getClass.getResourceAsStream("/tld2dns.json")
    val json = Source.fromInputStream(is).getLines().mkString("")
    println(json)
  }

  type Mapping = Map[String, String]

  val decodeDetailParam: Decoder[Mapping] = Decoder.instance { x => // TopCursor
    val keys = x.keys.getOrElse(List())

    val mappings = keys.flatMap { k =>
      println(s"k = $k")
      x.downField(k).get[String]("host") match {
        case Right(b) => Some(k -> b)
        case _ => None
      }
    }.toMap
    println(s"mappings = ${mappings}")

    Right(mappings)
  }

  val hostDecoder: Decoder[String] = Decoder[String].prepare(_.downField("host"))

  def parse(json: String): Either[Error, Mapping] = {
    decode[Map[String, String]](json)(decodeDetailParam)
  }

}
