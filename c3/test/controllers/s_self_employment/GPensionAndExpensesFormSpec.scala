package controllers.s_self_employment

import controllers.mappings.Mappings
import org.specs2.mutable._
import utils.WithApplication

class GPensionAndExpensesFormSpec extends Specification {
  section("unit", models.domain.SelfEmployment.id)
  "About Self Employment - Pension and Expenses Form" should {

    val yes = "yes"
    val no = "no"
    val pensionExpenses = "Some pension expenses"
    val jobExpenses = "Some job expenses"
    val overThreeHundredChars = "Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters"

    "map data into case class" in new WithApplication {
      GSelfEmploymentPensionsAndExpenses.form.bind(
        Map(
          "payPensionScheme.answer" -> no,
          "haveExpensesForJob.answer" -> no
          )
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.payPensionScheme.answer must equalTo(no)
          f.haveExpensesForJob.answer must equalTo(no)
        }
      )
    }

    "have 2 mandatory fields" in new WithApplication {
      GSelfEmploymentPensionsAndExpenses.form.bind(
        Map("payPensionScheme.text" -> pensionExpenses,
          "haveExpensesForJob.text" -> jobExpenses)
      ).fold(
          formWithErrors => {
            formWithErrors.errors.length must equalTo(2)
            formWithErrors.errors(0).message must equalTo(Mappings.errorRequired)
            formWithErrors.errors(1).message must equalTo(Mappings.errorRequired)
          },
          f => "This mapping should not happen." must equalTo("Valid")
        )
    }

    "reject if haveExpensesForJob is not filled" in new WithApplication {
      GSelfEmploymentPensionsAndExpenses.form.bind(
        Map("payPensionScheme.answer" -> no)
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo(Mappings.errorRequired),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject if payPensionScheme is not filled" in new WithApplication {
      GSelfEmploymentPensionsAndExpenses.form.bind(
        Map("haveExpensesForJob.answer" -> no)
      ).fold(
          formWithErrors => formWithErrors.errors.head.message must equalTo(Mappings.errorRequired),
          f => "This mapping should not happen." must equalTo("Valid")
        )
    }

    "have 1 expanded mandatory field if payPensionScheme is yes" in new WithApplication {
      GSelfEmploymentPensionsAndExpenses.form.bind(
        Map("haveExpensesForJob.answer" -> no,
          "payPensionScheme.answer" -> yes)
      ).fold(

          formWithErrors =>  {
            formWithErrors.errors.length must equalTo(1)
            formWithErrors.errors(0).message must equalTo("payPensionScheme.text.required")
          },
          f => "This mapping should not happen." must equalTo("Valid")
        )
    }

    "have 1 expanded mandatory field if haveExpensesForJob is yes" in new WithApplication {
      GSelfEmploymentPensionsAndExpenses.form.bind(
        Map("haveExpensesForJob.answer" -> yes,
          "payPensionScheme.answer" -> no)
      ).fold(

          formWithErrors =>  {
            formWithErrors.errors.length must equalTo(1)
            formWithErrors.errors(0).message must equalTo("haveExpensesForJob.text.required")
          },
          f => "This mapping should not happen." must equalTo("Valid")
        )
    }

    "reject too many characters in text fields" in new WithApplication {
      GSelfEmploymentPensionsAndExpenses.form.bind(
        Map("payPensionScheme.answer" -> yes,
          "haveExpensesForJob.answer" -> yes,
          "payPensionScheme.text" -> overThreeHundredChars,
          "haveExpensesForJob.text" -> overThreeHundredChars)
      ).fold(
          formWithErrors => {
            formWithErrors.errors.length must equalTo(2)
            formWithErrors.errors(0).message must equalTo(Mappings.maxLengthError)
            formWithErrors.errors(1).message must equalTo(Mappings.maxLengthError)
          },
          f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject special characters in text fields" in new WithApplication {
      GSelfEmploymentPensionsAndExpenses.form.bind(
        Map("payPensionScheme.answer" -> yes,
          "haveExpensesForJob.answer" -> yes,
          "payPensionScheme.text" -> "<>",
          "haveExpensesForJob.text" -> "<>"
        )
      ).fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(2)
          formWithErrors.errors(0).message must equalTo(Mappings.errorRestrictedCharacters)
          formWithErrors.errors(0).message must equalTo(Mappings.errorRestrictedCharacters)
        },
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }
  }
  section("unit", models.domain.SelfEmployment.id)
}
