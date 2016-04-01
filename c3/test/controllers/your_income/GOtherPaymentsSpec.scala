package controllers.your_income

import models.domain._
import org.specs2.mutable._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import utils.WithApplication
import utils.WithBrowser
import utils.pageobjects.your_income.GOtherPaymentsPage

class GOtherPaymentsSpec extends Specification {
  section ("unit", models.domain.OtherPayments.id)
  "Other Payments - Controller" should {
    val otherPayments = "Testing"

    val formInput = Seq(
      "otherPaymentsInfo" -> otherPayments
    )

    "present 'Other Payments '" in new WithApplication with Claiming {
      val request = FakeRequest()

      val result = GOtherPayments.present(request)

      status(result) mustEqual OK
    }

    "add submitted data to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest().withFormUrlEncodedBody(formInput: _*)

      val result = GOtherPayments.submit(request)
      val claim = getClaimFromCache(result)
      val section: Section = claim.section(YourIncomeOtherPayments)

      section.questionGroup(OtherPayments) must beLike {
        case Some(f: OtherPayments) => {
          f.otherPaymentsInfo must equalTo(otherPayments)
        }
      }
    }

    "missing mandatory fields" in new WithApplication with Claiming {
      val request = FakeRequest().withFormUrlEncodedBody("" -> "")

      val result = GOtherPayments.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "reject spaces in text" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(
          "otherPaymentsInfo" -> "    "
        )
      val result = GOtherPayments.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withFormUrlEncodedBody(formInput: _*)

      val result = GOtherPayments.submit(request)

      status(result) mustEqual SEE_OTHER
    }

    "have text maxlength set correctly in present()" in new WithBrowser {
      browser.goTo(GOtherPaymentsPage.url)
      val anythingElse = browser.$("#otherPaymentsInfo")
      val countdown = browser.$("#otherPaymentsInfo + .countdown")

      anythingElse.getAttribute("maxlength") mustEqual "3000"
      countdown.getText must contain( "3000 char")
      browser.pageSource must contain("maxChars:3000")
    }

    "reject over 3000 characters in text" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(
          "otherPaymentsInfo" -> "testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing"
        )
      val result = GOtherPayments.submit(request)
      status(result) mustEqual BAD_REQUEST
    }
  }
  section ("unit", models.domain.OtherPayments.id)
}
