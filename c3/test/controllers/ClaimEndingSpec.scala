package controllers

import play.api.mvc.{Result, Controller, Results}
import play.api.test.{FakeRequest, PlaySpecification}
import utils.WithApplication
import scala.concurrent.Future


class ClaimEndingSpec extends PlaySpecification with Results {

  class TestController() extends Controller

  "Claim ending" should {
    "return timeout page with claim start page if timeout()" in new WithApplication {
      val result: Future[Result] = ClaimEnding.timeout.apply(FakeRequest())
      val bodyText: String = contentAsString(result)
      bodyText must contain("You haven't entered any details")
      status(result) mustEqual REQUEST_TIMEOUT
    }

    "return error page with claim start page if error()" in new WithApplication {
      val result: Future[Result] = ClaimEnding.error.apply(FakeRequest())
      val bodyText: String = contentAsString(result)
      bodyText must contain("There's been a problem")
      status(result) mustEqual INTERNAL_SERVER_ERROR
    }

    "return error cookie page if errorCookie()" in new WithApplication {
      val result: Future[Result] = ClaimEnding.errorCookie.apply(FakeRequest())
      val bodyText: String = contentAsString(result)
      bodyText must contain("cookie")
      status(result) mustEqual UNAUTHORIZED
    }

    "return back buuton page if errorBrowserBackbutton()" in new WithApplication {
      val result: Future[Result] = ClaimEnding.errorBrowserBackbutton.apply(FakeRequest())
      val bodyText: String = contentAsString(result)
      bodyText must contain("web browser buttons")
      status(result) mustEqual BAD_REQUEST
    }

    "return ok thank you page if thankyou()" in new WithApplication {
      val result: Future[Result] = ClaimEnding.thankyou.apply(FakeRequest())
      val bodyText: String = contentAsString(result)
      bodyText must contain("been sent")
      status(result) mustEqual OK
    }

  } section "unit"

}
