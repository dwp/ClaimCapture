package controllers

import play.api.mvc.{SimpleResult, Controller, Results}
import play.api.test.{WithApplication, FakeRequest, PlaySpecification}
import scala.concurrent.Future

class RedirectSpec extends PlaySpecification with Results {
  class TestController() extends Controller with RedirectController

  "Redirect Page#redirect" should {
    "should be valid" in new WithApplication {
      val controller = new TestController()
      val result: Future[SimpleResult] = controller.redirect("a site").apply(FakeRequest())
      val bodyText: String = contentAsString(result)
      bodyText must contain("<h1>Please begin your claim or change of circumstance <a href='a site'>here</a></h1>")
    }
  } section "unit"
}
