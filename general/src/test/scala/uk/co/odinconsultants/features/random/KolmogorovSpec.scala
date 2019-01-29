package uk.co.odinconsultants.features.random

import org.scalatest.{Matchers, WordSpec}

class KolmogorovSpec extends WordSpec with Matchers {

  import Kolmogorov._

  "Input text" should {
    val ode = "I wandered lonely as a cloud"
    "be long than compressed text" in {
      score(ode) shouldBe > (ode.length)
    }
  }


}
