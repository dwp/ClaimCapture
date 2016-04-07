package controllers.your_income

import org.specs2.mutable._
import utils.WithApplication

class GDirectPaymentFormSpec extends Specification {
  section ("unit", models.domain.DirectPayment.id)
  "Direct Payment  Form" should {
    val whoPaysYou = "The Man"
    val howMuch = "12"
    val yes = "yes"
    val no = "no"
    val monthlyFrequency = "Monthly"

    "map data into case class" in new WithApplication {
      GDirectPayment.form.bind(
        Map(
          "stillBeingPaidThisPay_directPayment" -> yes,
          "whenDidYouLastGetPaid" -> "",
          "whoPaidYouThisPay_directPayment" -> whoPaysYou,
          "amountOfThisPay" -> howMuch,
          "howOftenPaidThisPay" -> monthlyFrequency,
          "howOftenPaidThisPayOther" -> ""
        )
      ).fold(
          formWithErrors => "This mapping should not happen." must equalTo("Error"),
          f => {
            f.stillBeingPaidThisPay must equalTo(yes)
            f.whenDidYouLastGetPaid must equalTo(None)
            f.whoPaidYouThisPay must equalTo(whoPaysYou)
            f.amountOfThisPay must equalTo(howMuch)
            f.howOftenPaidThisPay must equalTo(monthlyFrequency)
            f.howOftenPaidThisPayOther must equalTo(None)
          })
    }

    "reject invalid yesNo answers" in new WithApplication {
      GDirectPayment.form.bind(
        Map(
          "stillBeingPaidThisPay_directPayment" -> "INVALID"
        )
      ).fold(
          formWithErrors => {
            formWithErrors.errors.length must equalTo(4)
            formWithErrors.errors(0).message must equalTo("yesNo.invalid")
          },
          f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject a howOften frequency of other with no other text entered" in new WithApplication {
      GDirectPayment.form.bind(
        Map(
          "stillBeingPaidThisPay_directPayment" -> yes,
          "whenDidYouLastGetPaid" -> "",
          "whoPaidYouThisPay_directPayment" -> whoPaysYou,
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
      GDirectPayment.form.bind(
        Map(
          "stillBeingPaidThisPay_directPayment" -> no,
          "whenDidYouLastGetPaid" -> "",
          "whoPaidYouThisPay_directPayment" -> whoPaysYou,
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
  section ("unit", models.domain.DirectPayment.id)
}
