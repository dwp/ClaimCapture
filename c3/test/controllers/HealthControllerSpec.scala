package controllers

import play.api.mvc.{SimpleResult, Controller, Results}
import play.api.test.{WithApplication, FakeRequest, PlaySpecification}
import scala.concurrent.Future

class HealthControllerSpec extends PlaySpecification with Results {
  class TestController() extends Controller with HealthController

  "Health Page#health" should {
    "should be valid" in new WithApplication {
      val controller = new TestController()
      val result: Future[SimpleResult] = controller.health().apply(FakeRequest())
      val bodyText: String = contentAsString(result)
      bodyText must contain("Carer's allowance is functioning")
    }
  } section "unit"

  "Health Report" should {
    "should report true for db" in new WithApplication {
      val controller = new TestController()
      val result: Future[SimpleResult] = controller.healthReport().apply(FakeRequest())
      val bodyText: String = contentAsString(result)
      bodyText must contain("{\"name\":\"c3-transaction-db\",\"isHealthy\":true}")
    }
  } section "unit"
}
