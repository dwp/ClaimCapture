package controllers.your_income

import controllers.ClaimScenarioFactory._
import models.domain._
import org.specs2.mutable._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import utils.pageobjects.PageObjects
import utils.pageobjects.your_income.{GDirectPaymentPage, GYourIncomePage}
import utils.{WithBrowser, WithApplication}

class GDirectPaymentSpec extends Specification {
  section ("unit", models.domain.DirectPayment.id)
  "Direct Payment - Controller" should {
    val whoPaysYou = "The Man"
    val howMuch = "12"
    val yes = "yes"
    val monthlyFrequency = "Monthly"

    val formInput = Seq(
      "stillBeingPaidThisPay_directPayment" -> yes,
      "whenDidYouLastGetPaid" -> "",
      "whoPaidYouThisPay_directPayment" -> whoPaysYou,
      "amountOfThisPay" -> howMuch,
      "howOftenPaidThisPay" -> monthlyFrequency,
      "howOftenPaidThisPayOther" -> ""
    )

    "present 'Direct Payment '" in new WithBrowser with PageObjects {
      GYourIncomePage.fillYourIncomes(context, testData => { testData.YourIncomeDirectPay = "true" })
      val page = GDirectPaymentPage(context)
      page must beAnInstanceOf[GDirectPaymentPage]
    }

    "add submitted data to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(formInput: _*)

      val result = GDirectPayment.submit(request)
      val claim = getClaimFromCache(result)
      val section: Section = claim.section(YourIncomeDirectPayment)

      section.questionGroup(DirectPayment) must beLike {
        case Some(f: DirectPayment) => {
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
          "stillBeingPaidThisPay_directPayment" -> "INVALID",
          "whenDidYouLastGetPaid" -> "INVALID",
          "amountOfThisPay" -> "INVALID"
        )

      val result = GDirectPayment.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "missing mandatory fields" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody("" -> "")

      val result = GDirectPayment.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "reject a howOften frequency of other with no other text entered" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(
          "stillBeingPaidThisPay_directPayment" -> yes,
          "whenDidYouLastGetPaid" -> "",
          "whoPaidYouThisPay_directPayment" -> whoPaysYou,
          "amountOfThisPay" -> howMuch,
          "howOftenPaidThisPay" -> "Other",
          "howOftenPaidThisPayOther" -> ""
        )
      val result = GDirectPayment.submit(request)
      status(result) mustEqual BAD_REQUEST
    }


    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(formInput: _*)

      val result = GDirectPayment.submit(request)

      status(result) mustEqual SEE_OTHER
    }
  }
  section ("unit", models.domain.DirectPayment.id)
}
