package uk.co.odinconsultants.features.time.cycles

import java.time.LocalDateTime

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, WordSpec}

@RunWith(classOf[JUnitRunner])
class CyclesSpec extends WordSpec with Matchers {

  import Cycles._

  val tolerance = 0.0001

  def shouldBeSimilar(x: TimeAngle, y: TimeAngle): Unit = {
    x._1 shouldBe y._1 +- tolerance
    x._2 shouldBe y._2 +- tolerance
  }

  "Monday 00:00 and Sunday 23:59" should {
    val mondayMorning = LocalDateTime.of(2019, 2, 4, 0, 0)
    val sundayNight   = LocalDateTime.of(2019, 2, 3, 23, 59)

    "be close together on an hourly cycle" in {
      shouldBeSimilar(daily(mondayMorning), daily(sundayNight))
    }
    "be close together on a weekly cycle" in {
      shouldBeSimilar(weekly(mondayMorning), weekly(sundayNight))
    }
  }

}
