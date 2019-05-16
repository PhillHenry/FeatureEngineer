package uk.co.odinconsultants.features.domain_ip.address.name

trait PunyCode {

  // see https://www.irongeek.com/homoglyph-attack-generator.php
  val A: String = 0x0410.toChar.toString
  val F: String = 0x0046.toChar.toString
  val O: String = 0x041E.toChar.toString
  val E: String = 0x0415.toChar.toString
}
