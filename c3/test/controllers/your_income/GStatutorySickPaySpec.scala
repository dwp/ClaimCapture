package controllers.your_income

import controllers.ClaimScenarioFactory._
import models.domain._
import org.specs2.mutable._
import play.api.test.{FakeApplication, FakeRequest}
import utils.pageobjects.PageObjects
import utils.pageobjects.s_information.GAdditionalInfoPage
import utils.{WithBrowser, WithApplication}
import play.api.test.Helpers._
import utils.pageobjects.s_claim_date.GClaimDatePage
import utils.pageobjects.your_income.{GStatutorySickPayPage, GYourIncomePage}

class GStatutorySickPaySpec extends Specification {
  section ("unit", models.domain.StatutorySickPay.id)
  "Statutory Sick Pay - Controller" should {
    val whoPaysYou = "The Man"
    val howMuch = "12"
    val yes = "yes"
    val monthlyFrequency = "Monthly"

    val formInput = Seq(
      "stillBeingPaidThisPay" -> yes,
      "whenDidYouLastGetPaid" -> "",
      "whoPaidYouThisPay" -> whoPaysYou,
      "amountOfThisPay" -> howMuch,
      "howOftenPaidThisPay" -> monthlyFrequency,
      "howOftenPaidThisPayOther" -> ""
    )

    "present 'Statutory Sick Pay '" in new WithBrowser with PageObjects {
      GYourIncomePage.fillYourIncomes(context, testData => { testData.YourIncomeStatutorySickPay = "true" })
      val page = GStatutorySickPayPage(context)
      page must beAnInstanceOf[GStatutorySickPayPage]
    }

    "add submitted data to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(formInput: _*)

      val result = GStatutorySickPay.submit(request)
      val claim = getClaimFromCache(result)
      val section: Section = claim.section(YourIncomeStatutorySickPay)

      section.questionGroup(StatutorySickPay) must beLike {
        case Some(f: StatutorySickPay) => {
          f.stillBeingPaidThisPay must equalTo(yes)
          f.whenDidYouLastGetPaid must equalTo(None)
          f.whoPaidYouThisPay must equalTo(whoPaysYou)
          f.amountOfThisPay must equalTo(howMuch)
          f.howOftenPaidThisPay must equalTo(monthlyFrequency)
          f.howOftenPaidThisPayOther must equalTo(None)
        }
      }
    }

    "reject invalid yesNo answers" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(
          "stillBeingPaidThisPay" -> "INVALID",
          "whenDidYouLastGetPaid" -> "INVALID",
          "amountOfThisPay" -> "INVALID"
        )

      val result = GStatutorySickPay.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "missing mandatory fields" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody("" -> "")

      val result = GStatutorySickPay.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "reject a howOften frequency of other with no other text entered" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(
          "stillBeingPaidThisPay" -> yes,
          "whenDidYouLastGetPaid" -> "",
          "whoPaidYouThisPay" -> whoPaysYou,
          "amountOfThisPay" -> howMuch,
          "howOftenPaidThisPay" -> "Other",
          "howOftenPaidThisPayOther" -> ""
        )
      val result = GStatutorySickPay.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(formInput: _*)

      val result = GStatutorySickPay.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    "have text maxlength set correctly in present()" in new WithBrowser with PageObjects {
      GYourIncomePage.fillYourIncomes(context, testData => { testData.YourIncomeStatutorySickPay = "true" })
      val page = GStatutorySickPayPage(context)
      page must beAnInstanceOf[GStatutorySickPayPage]

      val whoPaidYouThisPay = browser.$("#whoPaidYouThisPay")
      whoPaidYouThisPay.getAttribute("maxlength") mustEqual "60"

      val amountOfThisPay = browser.$("#amountOfThisPay")
      amountOfThisPay.getAttribute("maxlength") mustEqual "12"

      val howOftenPaidThisPayOther = browser.$("#howOftenPaidThisPayOther")
      howOftenPaidThisPayOther.getAttribute("maxlength") mustEqual "60"
    }
  }
  section ("unit", models.domain.StatutorySickPay.id)
}
