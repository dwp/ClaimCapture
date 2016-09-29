package controllers.your_income

import org.specs2.mutable._
import utils.WithApplication

class GRentalIncomeFormSpec extends Specification {
  section ("unit", models.domain.RentalIncome.id)
  "Rental Income Form" should {
    val rentalIncome = "Some rent paid to me"

    "map data into case class" in new WithApplication {
      GRentalIncome.form.bind(
        Map(
          "rentalIncomeInfo" -> rentalIncome
        )
      ).fold(
          formWithErrors => "This mapping should not happen." must equalTo("Error"),
          f => {
            f.rentalIncomeInfo must equalTo(rentalIncome)
          })
    }

    "reject spaces in text" in new WithApplication {
      GRentalIncome.form.bind(
        Map(
          "rentalIncomeInfo" -> "   "
        )
      ).fold(
          formWithErrors => {
            formWithErrors.errors.length must equalTo(1)
            formWithErrors.errors(0).message must equalTo("error.required")
          },
          f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject too many characters" in new WithApplication {
      GRentalIncome.form.bind(
        Map(
          "rentalIncomeInfo" -> "testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing"
        )
      ).fold(
          formWithErrors => {
            formWithErrors.errors.length must equalTo(1)
            formWithErrors.errors(0).message must equalTo("error.maxLength")
          },
          f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject when missing mandatory fields" in new WithApplication {
      GRentalIncome.form.bind(
        Map(
          "nothing" -> ""
        )
      ).fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(1)
          formWithErrors.errors(0).message must equalTo("error.required")
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }
  }
  section ("unit", models.domain.RentalIncome.id)
}
