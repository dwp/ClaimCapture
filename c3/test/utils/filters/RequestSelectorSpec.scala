package utils.filters

import org.specs2.mutable._
import play.api.test.FakeRequest
import utils.WithApplication

class RequestSelectorSpec extends Specification {
  section("unit")
  "Request Selector" should {
    "not mark an error page as to be checked" in new WithApplication {
      val request = FakeRequest(method="GET",path="/error")
      RequestSelector.toBeChecked(request) must beFalse
      val request2 = FakeRequest(method="GET", path="/circs-error")
      RequestSelector.toBeChecked(request2) must beFalse
    }

    "not mark an asset request as to be checked" in new WithApplication {
      val request = FakeRequest(method="GET",path="/assets/stylesheets/header-footer-only-ie8.css")
      RequestSelector.toBeChecked(request) must beFalse
    }

    "mark allowance benefits and circumstances selection pages as start page" in new WithApplication {
      val request = FakeRequest(method="GET",path="/allowance/benefits")
      RequestSelector.startPage(request) must beTrue
      val request2 = FakeRequest(method="GET",path="/circumstances/report-changes/selection")
      RequestSelector.startPage(request2) must beTrue
    }

    "mark thank you pages and timeout pages as end page" in new WithApplication {
      val request = FakeRequest(method="GET",path="/thankyou/apply-carers")
      RequestSelector.endPage(request) must beTrue
      val request2 = FakeRequest(method="GET",path="/thankyou/change-carers")
      RequestSelector.endPage(request2) must beTrue
      val request3 = FakeRequest(method="GET",path="/timeout")
      RequestSelector.endPage(request3) must beTrue
      val request4 = FakeRequest(method="GET",path="/circs-timeout")
      RequestSelector.endPage(request4) must beTrue
    }
  }
  section("unit")
}
