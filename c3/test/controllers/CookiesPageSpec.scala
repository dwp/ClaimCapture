package controllers

import org.specs2.mutable._
import play.api.test.FakeRequest
import play.api.test.Helpers.OK
import play.api.test.Helpers.status
import utils.WithApplication
import akka.util.Timeout
import java.util.concurrent.TimeUnit

class CookiesPageSpec extends Specification {

  implicit val timeout = Timeout(10, TimeUnit.SECONDS)

  "Cookies page" should {
    "Present" in new WithApplication {
      val request = FakeRequest()
      val result = controllers.Cookies.page("en")(request)
      status(result) mustEqual OK
    }
  }
section("unit")

}
