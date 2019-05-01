package uk.co.odinconsultants.features.spark

import org.scalatest.{Matchers, WordSpec}

class EnhancementSpec extends WordSpec with Matchers {

  import Enhancement._

  val now = new java.sql.Date(new java.util.Date().getTime)

  "The 'nNoOp' data" should {
    "be further in the future than now" in {
      NoDate.after(now) shouldBe true
    }
  }

}
