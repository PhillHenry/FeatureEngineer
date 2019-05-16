package uk.co.odinconsultants.features.domain_ip.address.name

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, WordSpec}

@RunWith(classOf[JUnitRunner])
class CleanerSpec extends WordSpec with Matchers {

  import Cleaner._

  "CVS" should {
    "map to single unicode characters" in {
      val xs = csv2Unicode("5f,7fa,fe4d,fe4e,fe4f,ff3f")
      xs should have size 6
      xs foreach { x =>
        withClue(s"$x in $xs") {
          x.length shouldBe 1
        }
      }
    }
  }

  "Non-ascii characters" should {
    "map to ascii" in new VodafoneFixture {
      similar foreach { w =>
        toSimilarAscii(w) foreach { c =>
          withClue(s"%04X ($c) in $w instead maps to: ${x2Ascii.getOrElse(c.toString, "unknown")}\n${x2Ascii.keys.map(_.charAt(0)).toList.sorted.map(_.toHexString).mkString("\n")}\n".format(c & 0xFFFFF)) {
            asciiUnicde should contain (c.toString)
          }
        }
      }
    }
  }

}
