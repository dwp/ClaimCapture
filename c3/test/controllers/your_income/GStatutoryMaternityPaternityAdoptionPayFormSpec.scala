package controllers.your_income

import org.specs2.mutable._
import utils.WithApplication

class GStatutoryMaternityPaternityAdoptionPayFormSpec extends Specification {
  section ("unit", models.domain.StatutoryMaternityPaternityAdoptionPay.id)
  "Statutory Maternity Paternity Adoption Pay Form" should {
    val whoPaysYou = "The Man"
    val howMuch = "12"
    val yes = "yes"
    val no = "no"
    val monthlyFrequency = "Monthly"
    val paymentType = "MaternityOrPaternityPay"

    "map data into case class" in new WithApplication {
      GStatutoryMaternityPaternityAdoptionPay.form.bind(
        Map(
          "paymentTypesForThisPay" -> paymentType,
          "stillBeingPaidThisPay_paternityMaternityAdoption" -> yes,
          "whenDidYouLastGetPaid" -> "",
          "whoPaidYouThisPay_paternityMaternityAdoption" -> whoPaysYou,
          "amountOfThisPay" -> howMuch,
          "howOftenPaidThisPay" -> monthlyFrequency,
          "howOftenPaidThisPayOther" -> ""
        )
      ).fold(
          formWithErrors => "This mapping should not happen." must equalTo("Error"),
          f => {
            f.paymentTypesForThisPay must equalTo(paymentType)
            f.stillBeingPaidThisPay must equalTo(yes)
            f.whenDidYouLastGetPaid must equalTo(None)
            f.whoPaidYouThisPay must equalTo(whoPaysYou)
            f.amountOfThisPay must equalTo(howMuch)
            f.howOftenPaidThisPay must equalTo(monthlyFrequency)
            f.howOftenPaidThisPayOther must equalTo(None)
          })
    }

    "reject invalid yesNo answers" in new WithApplication {
      GStatutoryMaternityPaternityAdoptionPay.form.bind(
        Map(
          "stillBeingPaidThisPay_paternityMaternityAdoption" -> "INVALID"
        )
      ).fold(
          formWithErrors => {
            formWithErrors.errors.length must equalTo(5)
            formWithErrors.errors(0).message must equalTo("error.required")
            formWithErrors.errors(1).message must equalTo("yesNo.invalid")
          },
          f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject a howOften frequency of other with no other text entered" in new WithApplication {
      GStatutoryMaternityPaternityAdoptionPay.form.bind(
        Map(
          "paymentTypesForThisPay" -> paymentType,
          "stillBeingPaidThisPay_paternityMaternityAdoption" -> yes,
          "whenDidYouLastGetPaid" -> "",
          "whoPaidYouThisPay_paternityMaternityAdoption" -> whoPaysYou,
          "amountOfThisPay" -> howMuch,
          "howOftenPaidThisPay" -> "Other",
          "howOftenPaidThisPayOther" -> ""
        )
      ).fold(
          formWithErrors => {
            formWithErrors.errors.length must equalTo(1)
            formWithErrors.errors(0).message must equalTo("howOftenPaidThisPay.required")
          },
          f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject when last paid blank when answer is no" in new WithApplication {
      GStatutoryMaternityPaternityAdoptionPay.form.bind(
        Map(
          "paymentTypesForThisPay" -> paymentType,
          "stillBeingPaidThisPay_paternityMaternityAdoption" -> no,
          "whenDidYouLastGetPaid" -> "",
          "whoPaidYouThisPay_paternityMaternityAdoption" -> whoPaysYou,
          "amountOfThisPay" -> howMuch,
          "howOftenPaidThisPay" -> monthlyFrequency,
          "howOftenPaidThisPayOther" -> ""
        )
      ).fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(1)
          formWithErrors.errors(0).message must equalTo("whenDidYouLastGetPaid.required")
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }

  }
  section ("unit", models.domain.StatutoryMaternityPaternityAdoptionPay.id)
}
