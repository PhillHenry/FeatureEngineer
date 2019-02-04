package uk.co.odinconsultants.features.time.cycles

import java.time.{LocalDateTime, ZoneOffset}

/**
  * "You can't properly represent the months, a looping time structure, in 1D because loops can't exist in 1D. If you
  * lay the points out from 1 to 12, then December and January will not be adjacent. If you try to double back to place
  * them close, then you end up with months like April and October being extremely close even though they are not.
  * To encode a loop, you need at least two dimensions. A circle is then natural choice for the loop because of unit
  * norm properties"
  *
  * See https://www.reddit.com/r/MLQuestions/comments/8i5y1k/normalise_date_field/
  */
object Cycles {

  /**
    * Arbitrary if used consistently
    */
  val TIMEZONE: ZoneOffset  = ZoneOffset.of("Z")

  val MIN_MS:   Long        = 60000L
  val HOUR_MS:  Long        = MIN_MS * 60L
  val DAY_MS:   Long        = HOUR_MS * 24L
  val WEEK_MS:  Long        = DAY_MS * 7L
  val WEEK_2PI: Double      = (2 * math.Pi) / WEEK_MS

  type TimeAngle = (Double, Double)

  def weekly(x: LocalDateTime): TimeAngle = {
    val l         = x.toEpochSecond(TIMEZONE)
    val msInWeek  = l % WEEK_MS
    (math.sin(WEEK_2PI * msInWeek), math.cos(WEEK_2PI * msInWeek))
  }

}
