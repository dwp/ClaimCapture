package controllers.your_income

import org.specs2.mutable._
import utils.WithApplication

class GYourIncomesFormSpec extends Specification {
  section ("unit", models.domain.YourIncomes.id)
  "Statutory Sick Pay Form" should {
    val yes = "yes"

    "map data into case class" in new WithApplication {
      GYourIncomes.form.bind(
        Map(
          "beenSelfEmployedSince1WeekBeforeClaim" -> yes,
          "beenEmployedSince6MonthsBeforeClaim" -> yes,
          "yourIncome_sickpay" -> "true",
          "yourIncome_patmatadoppay" -> "true",
          "yourIncome_fostering" -> "true",
          "yourIncome_directpay" -> "true",
          "yourIncome_anyother" -> "true"
        )
      ).fold(
          formWithErrors => "This mapping should not happen." must equalTo("Error"),
          f => {
            f.beenSelfEmployedSince1WeekBeforeClaim must equalTo(yes)
            f.beenEmployedSince6MonthsBeforeClaim must equalTo(yes)
            f.yourIncome_sickpay must equalTo(Some("true"))
            f.yourIncome_patmatadoppay must equalTo(Some("true"))
            f.yourIncome_fostering must equalTo(Some("true"))
            f.yourIncome_directpay must equalTo(Some("true"))
            f.yourIncome_anyother must equalTo(Some("true"))
          })
    }

    "reject invalid yesNo answers" in new WithApplication {
      GYourIncomes.form.bind(
        Map(
          "beenSelfEmployedSince1WeekBeforeClaim" -> "INVALID",
          "beenEmployedSince6MonthsBeforeClaim" -> "INVALID"
        )
      ).fold(
          formWithErrors => {
            formWithErrors.errors.length must equalTo(2)
            formWithErrors.errors(0).message must equalTo("yesNo.invalid")
            formWithErrors.errors(1).message must equalTo("yesNo.invalid")
          },
          f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject with all incomes selected" in new WithApplication {
      GYourIncomes.form.bind(
        Map(
          "beenSelfEmployedSince1WeekBeforeClaim" -> yes,
          "beenEmployedSince6MonthsBeforeClaim" -> yes,
          "yourIncome_sickpay" -> "true",
          "yourIncome_patmatadoppay" -> "true",
          "yourIncome_fostering" -> "true",
          "yourIncome_directpay" -> "true",
          "yourIncome_anyother" -> "true",
          "yourIncome_none" -> "true"
        )
      ).fold(
          formWithErrors => {
            formWithErrors.errors.length must equalTo(1)
            formWithErrors.errors(0).message must equalTo("required")
          },
          f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject when nothing selected" in new WithApplication {
      GYourIncomes.form.bind(
        Map(
          "beenSelfEmployedSince1WeekBeforeClaim" -> yes,
          "beenEmployedSince6MonthsBeforeClaim" -> yes
        )
      ).fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(1)
          formWithErrors.errors(0).message must equalTo("value.required")
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject when none and one other selected" in new WithApplication {
      GYourIncomes.form.bind(
        Map(
          "beenSelfEmployedSince1WeekBeforeClaim" -> yes,
          "beenEmployedSince6MonthsBeforeClaim" -> yes,
          "yourIncome_anyother" -> "true",
          "yourIncome_none" -> "true"
        )
      ).fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(1)
          formWithErrors.errors(0).message must equalTo("required")
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }

  }
  section ("unit", models.domain.StatutorySickPay.id)
}
