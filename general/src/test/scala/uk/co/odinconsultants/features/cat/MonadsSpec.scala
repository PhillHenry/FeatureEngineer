package uk.co.odinconsultants.features.cat

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, WordSpec}

@RunWith(classOf[JUnitRunner])
class MonadsSpec extends WordSpec with Matchers {

  import Monads._

  "String monads" should {
    "concatenate" in {
      val m     = implicitly[Monads[String]]
      val text  = "12345"
      m.op(text.split("")) shouldBe text
    }
  }

}
