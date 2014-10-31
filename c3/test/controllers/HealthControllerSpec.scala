package controllers

import play.api.mvc.{Result, Controller, Results}
import play.api.test.{WithApplication, FakeRequest, PlaySpecification}
import scala.concurrent.Future

class HealthControllerSpec extends PlaySpecification with Results {
  args(skipAll=true)

  class TestController() extends Controller with HealthController

  "Health Page#health" should {
    "should be valid" in new WithApplication {
      val controller = new TestController()
      val result: Future[Result] = controller.health().apply(FakeRequest())
      val bodyText: String = contentAsString(result)
      bodyText must contain("Carer's allowance is functioning")
    }
  } section "unit"

  "Health Report" should {
    "should report true for db" in new WithApplication {
      val controller = new TestController()
      val result: Future[Result] = controller.healthReport().apply(FakeRequest())
      val bodyText: String = contentAsString(result)
      bodyText must contain("[ {\n  \"name\" : \"c3-cache\",\n  \"isHealthy\" : true\n}, {\n  \"name\" : \"c3-transaction-db\",\n  \"isHealthy\" : true\n} ]")
    }
  } section "unit"
}




