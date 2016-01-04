package utils.filters

import org.specs2.mutable._
import play.api.test.FakeRequest
import play.mvc.Http.HeaderNames
import utils.WithApplication

class CSRFCreationSpec extends Specification {
  section("unit")
  "CSRF Creation object" should {
    "identifies GET calls for html for in app pages as candidates for CSRF token." in new WithApplication {
      val request = FakeRequest(method = "GET", path = "/allowance/approve").withHeaders(HeaderNames.ACCEPT -> "text/html")
      CSRFCreation.createIfNotFound(request) must beTrue
    }

    "identifies POST calls even for html in app pages as NOT candidates for CSRF token." in new WithApplication {
      val request = FakeRequest(method = "POST", path = "/allowance/approve").withHeaders(HeaderNames.ACCEPT -> "text/html")
      CSRFCreation.createIfNotFound(request) must beFalse
    }

    "identifies start pages as pages where token has to be created." in new WithApplication {
      val request = FakeRequest(method = "GET", path = "/allowance/benefits")
      CSRFCreation.createIfFound(request) must beTrue
      val request2 = FakeRequest(method = "GET", path = "/circumstances/report-changes/selection")
      CSRFCreation.createIfFound(request2) must beTrue
    }
  }
  section("unit")
}
