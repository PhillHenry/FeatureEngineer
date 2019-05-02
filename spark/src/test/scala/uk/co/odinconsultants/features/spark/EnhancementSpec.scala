package uk.co.odinconsultants.features.spark

import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.ExecutionContext

class EnhancementSpec extends WordSpec with Matchers {

  implicit val ec = ExecutionContext.global

  import Enhancement._

  val CANCELLED = new Object
  val SUCCESS = new Object

  def longRunningFn: Object = {
    try {
      Thread.sleep(Long.MaxValue)
      SUCCESS
    } catch {
      case e: InterruptedException => CANCELLED
    }
  }

  def fastFunction: Object = SUCCESS

  "Long running thread" should {
    "be timed out" in {
      stopMe(longRunningFn, 200) shouldBe CANCELLED
      Thread.interrupted() shouldBe false
    }
  }

  "Fast thread" should {
    "return a value" in {
      stopMe(fastFunction, 200) shouldBe SUCCESS
      Thread.interrupted() shouldBe false
    }
  }

}
