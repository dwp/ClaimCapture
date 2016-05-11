package controllers.your_income

import models.domain._
import org.specs2.mutable._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import utils.pageobjects.PageObjects
import utils.{WithBrowser, WithApplication}
import utils.pageobjects.your_income.{GFosteringAllowancePage, GYourIncomePage}

class GFosteringAllowanceSpec extends Specification {
  section ("unit", models.domain.FosteringAllowance.id)
  "Fostering Allowance - Controller" should {
    val whoPaysYou = "The Man"
    val howMuch = "12"
    val yes = "yes"
    val monthlyFrequency = "Monthly"
    val paymentType = "FosteringAllowance"

    val formInput = Seq(
      "fosteringAllowancePay" -> paymentType,
      "stillBeingPaidThisPay_fosteringAllowance" -> yes,
      "whenDidYouLastGetPaid" -> "",
      "whoPaidYouThisPay_fosteringAllowance" -> whoPaysYou,
      "amountOfThisPay" -> howMuch,
      "howOftenPaidThisPay" -> monthlyFrequency,
      "howOftenPaidThisPayOther" -> ""
    )

    "present 'Fostering Allowance '" in new WithBrowser with PageObjects {
      GYourIncomePage.fillYourIncomes(context, testData => { testData.YourIncomeFosteringAllowance = "true" })
      val page = GFosteringAllowancePage(context)
      page must beAnInstanceOf[GFosteringAllowancePage]
    }

    "add submitted data to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest().withFormUrlEncodedBody(formInput: _*)

      val result = GFosteringAllowance.submit(request)
      val claim = getClaimFromCache(result)
      val section: Section = claim.section(YourIncomeFosteringAllowance)

      section.questionGroup(FosteringAllowance) must beLike {
        case Some(f: FosteringAllowance) => {
          f.paymentTypesForThisPay must equalTo(paymentType)
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
          "stillBeingPaidThisPay_fosteringAllowance" -> "INVALID",
          "whenDidYouLastGetPaid" -> "INVALID",
          "amountOfThisPay" -> "INVALID"
        )

      val result = GFosteringAllowance.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "missing mandatory fields" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody("" -> "")

      val result = GFosteringAllowance.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "reject a howOften frequency of other with no other text entered" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(
          "fosteringAllowancePay" -> paymentType,
          "stillBeingPaidThisPay_fosteringAllowance" -> yes,
          "whenDidYouLastGetPaid" -> "",
          "whoPaidYouThisPay_fosteringAllowance" -> whoPaysYou,
          "amountOfThisPay" -> howMuch,
          "howOftenPaidThisPay" -> "Other",
          "howOftenPaidThisPayOther" -> ""
        )
      val result = GFosteringAllowance.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(formInput: _*)

      val result = GFosteringAllowance.submit(request)

      status(result) mustEqual SEE_OTHER
    }

    "reject a payment type of other with no other text entered" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(
          "fosteringAllowancePay" -> "Other",
          "stillBeingPaidThisPay_fosteringAllowance" -> yes,
          "whenDidYouLastGetPaid" -> "",
          "whoPaidYouThisPay_fosteringAllowance" -> whoPaysYou,
          "amountOfThisPay" -> howMuch,
          "howOftenPaidThisPay" -> monthlyFrequency
        )
      val result = GFosteringAllowance.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "have text maxlength set correctly in present()" in new WithBrowser with PageObjects {
      GYourIncomePage.fillYourIncomes(context, testData => { testData.YourIncomeFosteringAllowance = "true" })
      val page = GFosteringAllowancePage(context)
      page must beAnInstanceOf[GFosteringAllowancePage]

      val whoPaidYouOther = browser.$("#fosteringAllowancePayOther")
      whoPaidYouOther.getAttribute("maxlength") mustEqual "60"

      val whoPaidYouThisPay = browser.$("#whoPaidYouThisPay_fosteringAllowance")
      whoPaidYouThisPay.getAttribute("maxlength") mustEqual "60"

      val amountOfThisPay = browser.$("#amountOfThisPay")
      amountOfThisPay.getAttribute("maxlength") mustEqual "12"

      val howOftenPaidThisPayOther = browser.$("#howOftenPaidThisPayOther")
      howOftenPaidThisPayOther.getAttribute("maxlength") mustEqual "60"
    }
  }
  section ("unit", models.domain.FosteringAllowance.id)
}
