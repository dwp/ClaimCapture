package controllers.your_income

import org.specs2.mutable._
import utils.WithApplication

class GStatutorySickPayFormSpec extends Specification {
  section ("unit", models.domain.StatutorySickPay.id)
  "Statutory Sick Pay Form" should {
    val whoPaysYou = "The Man"
    val howMuch = "12"
    val yes = "yes"
    val no = "no"

    "map data into case class" in new WithApplication {
      GStatutorySickPay.form.bind(
        Map(
          "stillBeingPaidStatutorySickPay" -> yes,
          "whenDidYouLastGetPaid" -> "",
          "whoPaidYouStatutorySickPay" -> whoPaysYou,
          "amountOfStatutorySickPay" -> howMuch,
          "howOftenPaidStatutorySickPay" -> "Monthly",
          "howOftenPaidStatutorySickPayOther" -> ""
        )
      ).fold(
          formWithErrors => "This mapping should not happen." must equalTo("Error"),
          f => {
            f.stillBeingPaidStatutorySickPay must equalTo(yes)
            f.whenDidYouLastGetPaid must equalTo(None)
            f.whoPaidYouStatutorySickPay must equalTo(whoPaysYou)
            f.amountOfStatutorySickPay must equalTo(howMuch)
            f.howOftenPaidStatutorySickPay must equalTo("Monthly")
            f.howOftenPaidStatutorySickPayOther must equalTo(None)
          })
    }

    "reject invalid yesNo answers" in new WithApplication {
      GStatutorySickPay.form.bind(
        Map(
          "stillBeingPaidStatutorySickPay" -> "INVALID"
        )
      ).fold(
          formWithErrors => {
            formWithErrors.errors.length must equalTo(4)
            formWithErrors.errors(0).message must equalTo("yesNo.invalid")
          },
          f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject a howOften frequency of other with no other text entered - statutory sick pay" in new WithApplication {
      GStatutorySickPay.form.bind(
        Map(
          "stillBeingPaidStatutorySickPay" -> yes,
          "whenDidYouLastGetPaid" -> "",
          "whoPaidYouStatutorySickPay" -> whoPaysYou,
          "amountOfStatutorySickPay" -> howMuch,
          "howOftenPaidStatutorySickPay" -> "Other",
          "howOftenPaidStatutorySickPayOther" -> ""
        )
      ).fold(
          formWithErrors => {
            formWithErrors.errors.length must equalTo(1)
            formWithErrors.errors(0).message must equalTo("error.paymentFrequency")
          },
          f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject when last paid blank when answer is no" in new WithApplication {
      GStatutorySickPay.form.bind(
        Map(
          "stillBeingPaidStatutorySickPay" -> no,
          "whenDidYouLastGetPaid" -> "",
          "whoPaidYouStatutorySickPay" -> whoPaysYou,
          "amountOfStatutorySickPay" -> howMuch,
          "howOftenPaidStatutorySickPay" -> "Monthly",
          "howOftenPaidStatutorySickPayOther" -> ""
        )
      ).fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(1)
          formWithErrors.errors(0).message must equalTo("error.paymentFrequency")
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }

  }
  section ("unit", models.domain.StatutorySickPay.id)
}
