package uk.co.odinconsultants.features.time.cycles

import java.time.LocalDateTime

import org.scalatest.{Matchers, WordSpec}

class CyclesSpec extends WordSpec with Matchers {

  import Cycles._

  val tolerance = 0.0001

  "Monday morning and Sunday nigh" should {
    val mondayMorning = LocalDateTime.of(2019, 2, 4, 0, 0)
    val sundayNight   = LocalDateTime.of(2019, 2, 3, 23, 59)
    "should be close together" in {
      val x = weekly(mondayMorning)
      val y = weekly(sundayNight)

      x._1 shouldBe y._1 +- tolerance
      x._2 shouldBe y._2 +- tolerance
    }
  }

}
