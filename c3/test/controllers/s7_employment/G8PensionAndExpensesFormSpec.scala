package controllers.s7_employment

import org.specs2.mutable.{Tags, Specification}

class G8PensionAndExpensesFormSpec extends Specification with Tags {
  "About Employment - Pension and Expenses Form" should {
    val jobId = "1"
    val yes = "yes"
    val no = "no"
    val pensionExpenses = "Some pension expenses"
    val payForThings = "Some things paid for to do the job"
    val jobExpenses = "Some job expenses"
    val overThreeHundredChars = "Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters"

    "map data into case class" in {
      G8PensionAndExpenses.form.bind(
        Map(
          "iterationID" -> jobId,
          "payPensionScheme.answer" -> no,
          "payForThings.answer" -> no,
          "haveExpensesForJob.answer" -> no
          )
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.iterationID must equalTo(jobId)
          f.payPensionScheme.answer must equalTo(no)
          f.payForThings.answer must equalTo(no)
          f.haveExpensesForJob.answer must equalTo(no)
        }
      )
    }

    "have 3 mandatory fields" in {
      G8PensionAndExpenses.form.bind(
        Map("iterationID" -> jobId)
      ).fold(
          formWithErrors => {
            formWithErrors.errors.length must equalTo(3)
            formWithErrors.errors(0).message must equalTo("error.required")
            formWithErrors.errors(1).message must equalTo("error.required")
            formWithErrors.errors(2).message must equalTo("error.required")
          },
          f => "This mapping should not happen." must equalTo("Valid")
        )
    }

    "reject if haveExpensesForJob is not filled" in {
      G8PensionAndExpenses.form.bind(
        Map(
          "iterationID" -> jobId,
          "payPensionScheme.answer" -> no,
          "payForThings.answer" -> no)
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject if payPensionScheme is not filled" in {
      G8PensionAndExpenses.form.bind(
        Map(
          "iterationID" -> jobId,
          "payForThings.answer" -> no,
          "haveExpensesForJob.answer" -> no)
      ).fold(
          formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
          f => "This mapping should not happen." must equalTo("Valid")
        )
    }

    "reject if pay for things is not filled" in {
      G8PensionAndExpenses.form.bind(
        Map(
          "iterationID" -> jobId,
          "payPensionScheme.answer" -> no,
          "haveExpensesForJob.answer" -> no)
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "have 1 expanded mandatory field if payPensionScheme is yes" in {
      G8PensionAndExpenses.form.bind(
        Map(
          "iterationID" -> jobId,
          "payForThings.answer" -> no,
          "haveExpensesForJob.answer" -> no,
          "payPensionScheme.answer" -> yes)
      ).fold(

          formWithErrors =>  {
            formWithErrors.errors.length must equalTo(1)
            formWithErrors.errors(0).message must equalTo("payPensionScheme.text.required")
          },
          f => "This mapping should not happen." must equalTo("Valid")
        )
    }

    "have 1 expanded mandatory field if haveExpensesForJob is yes" in {
      G8PensionAndExpenses.form.bind(
        Map(
          "iterationID" -> jobId,
          "payForThings.answer" -> no,
          "haveExpensesForJob.answer" -> yes,
          "payPensionScheme.answer" -> no)
      ).fold(

          formWithErrors =>  {
            formWithErrors.errors.length must equalTo(1)
            formWithErrors.errors(0).message must equalTo("haveExpensesForJob.text.required")
          },
          f => "This mapping should not happen." must equalTo("Valid")
        )
    }

    "reject if details for the things paid for not filled when answered yes" in {
        G8PensionAndExpenses.form.bind(
          Map(
            "iterationID" -> jobId,
            "payForThings.answer" -> yes,
            "haveExpensesForJob.answer" -> no,
            "payPensionScheme.answer" -> no)
        ).fold(

          formWithErrors =>  {
            formWithErrors.errors.length must equalTo(1)
            formWithErrors.errors(0).message must equalTo("payForThings.text.required")
          },
          f => "This mapping should not happen." must equalTo("Valid")
        )
    }

    "reject too many characters in text fields" in {
      G8PensionAndExpenses.form.bind(
        Map(
          "iterationID" -> jobId,
          "payPensionScheme.answer" -> yes,
          "payForThings.answer" -> yes,
          "haveExpensesForJob.answer" -> yes,
          "payPensionScheme.text" -> overThreeHundredChars,
          "payForThings.text" -> overThreeHundredChars,
          "haveExpensesForJob.text" -> overThreeHundredChars)
      ).fold(
          formWithErrors => {
            formWithErrors.errors.length must equalTo(3)
            formWithErrors.errors(0).message must equalTo("error.maxLength")
            formWithErrors.errors(1).message must equalTo("error.maxLength")
            formWithErrors.errors(2).message must equalTo("error.maxLength")
          },
          f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject special characters in text fields" in {
      G8PensionAndExpenses.form.bind(
        Map(
          "iterationID" -> jobId,
          "payPensionScheme.answer" -> yes,
          "payForThings.answer" -> yes,
          "haveExpensesForJob.answer" -> yes,
          "payPensionScheme.text" -> "<>",
          "payForThings.text" -> "<>",
          "haveExpensesForJob.text" -> "<>"
        )
      ).fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(3)
          formWithErrors.errors(0).message must equalTo("error.restricted.characters")
          formWithErrors.errors(1).message must equalTo("error.restricted.characters")
          formWithErrors.errors(2).message must equalTo("error.restricted.characters")
        },
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }
  } section("unit", models.domain.Employment.id)
}