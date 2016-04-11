package controllers.your_income

import models.domain._
import org.specs2.mutable._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import utils.pageobjects.PageObjects
import utils.pageobjects.your_income.{GYourIncomePage, GStatutoryMaternityPaternityAdoptionPayPage}
import utils.{WithBrowser, WithApplication}

class GStatutoryMaternityPaternityAdoptionPaySpec extends Specification {
  section ("unit", models.domain.StatutoryMaternityPaternityAdoptionPay.id)
  "Statutory Maternity Paternity Adoption Pay - Controller" should {
    val whoPaysYou = "The Man"
    val howMuch = "12"
    val yes = "yes"
    val monthlyFrequency = "Monthly"
    val paymentType = "MaternityOrPaternityPay"

    val formInput = Seq(
      "paymentTypesForThisPay" -> paymentType,
      "stillBeingPaidThisPay_paternityMaternityAdoption" -> yes,
      "whenDidYouLastGetPaid" -> "",
      "whoPaidYouThisPay_paternityMaternityAdoption" -> whoPaysYou,
      "amountOfThisPay" -> howMuch,
      "howOftenPaidThisPay" -> monthlyFrequency,
      "howOftenPaidThisPayOther" -> ""
    )

    "present 'Statutory Maternity Paternity Adoption Pay '" in new WithBrowser with PageObjects {
      GYourIncomePage.fillYourIncomes(context, testData => { testData.YourIncomePatMatAdopPay = "true" })
      val page = GStatutoryMaternityPaternityAdoptionPayPage(context)
      page must beAnInstanceOf[GStatutoryMaternityPaternityAdoptionPayPage]
    }

    "add submitted data to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(formInput: _*)

      val result = GStatutoryMaternityPaternityAdoptionPay.submit(request)
      val claim = getClaimFromCache(result)
      val section: Section = claim.section(YourIncomeStatutoryMaternityPaternityAdoptionPay)

      section.questionGroup(StatutoryMaternityPaternityAdoptionPay) must beLike {
        case Some(f: StatutoryMaternityPaternityAdoptionPay) => {
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
          "stillBeingPaidThisPay_paternityMaternityAdoption" -> "INVALID",
          "whenDidYouLastGetPaid" -> "INVALID",
          "amountOfThisPay" -> "INVALID"
        )

      val result = GStatutoryMaternityPaternityAdoptionPay.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "missing mandatory fields" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody("" -> "")

      val result = GStatutoryMaternityPaternityAdoptionPay.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "reject a howOften frequency of other with no other text entered" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(
          "paymentTypesForThisPay" -> paymentType,
          "stillBeingPaidThisPay_paternityMaternityAdoption" -> yes,
          "whenDidYouLastGetPaid" -> "",
          "whoPaidYouThisPay_paternityMaternityAdoption" -> whoPaysYou,
          "amountOfThisPay" -> howMuch,
          "howOftenPaidThisPay" -> "Other",
          "howOftenPaidThisPayOther" -> ""
        )
      val result = GStatutoryMaternityPaternityAdoptionPay.submit(request)
      status(result) mustEqual BAD_REQUEST
    }


    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(formInput: _*)

      val result = GStatutoryMaternityPaternityAdoptionPay.submit(request)

      status(result) mustEqual SEE_OTHER
    }

    "have text maxlength set correctly in present()" in new WithBrowser with PageObjects {
      GYourIncomePage.fillYourIncomes(context, testData => { testData.YourIncomePatMatAdopPay = "true" })
      val page = GStatutoryMaternityPaternityAdoptionPayPage(context)
      page must beAnInstanceOf[GStatutoryMaternityPaternityAdoptionPayPage]

      val whoPaidYouThisPay = browser.$("#whoPaidYouThisPay_paternityMaternityAdoption")
      whoPaidYouThisPay.getAttribute("maxlength") mustEqual "60"

      val amountOfThisPay = browser.$("#amountOfThisPay")
      amountOfThisPay.getAttribute("maxlength") mustEqual "12"

      val howOftenPaidThisPayOther = browser.$("#howOftenPaidThisPayOther")
      howOftenPaidThisPayOther.getAttribute("maxlength") mustEqual "60"
    }
  }
  section ("unit", models.domain.StatutoryMaternityPaternityAdoptionPay.id)
}
