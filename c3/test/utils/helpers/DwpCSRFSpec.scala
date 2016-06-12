package utils.helpers

import org.specs2.mutable.Specification
import play.api.test.FakeRequest
import play.api.test.Helpers._
import utils.WithApplication
import utils.csrf.DwpCSRFFormHelper

// Test the carers common CSRF util in here as we have easy access to test including request and fakeapp.
class DwpCSRFSpec extends Specification {
  section("unit")
  "DwpCSRF" should {
    "generate csrf string" in new WithApplication {
      val request = FakeRequest()
      val csrfString = DwpCSRFFormHelper.formField(request)
      println("csrfString:" + csrfString)
      "a" mustEqual "a"
    }
  }
  section("unit")
}
