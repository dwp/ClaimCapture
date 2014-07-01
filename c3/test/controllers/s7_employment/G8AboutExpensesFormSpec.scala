package controllers.s7_employment

import org.specs2.mutable.{Tags, Specification}
import app.PensionPaymentFrequency._
import models.PensionPaymentFrequency

class G8AboutExpensesFormSpec extends Specification with Tags {
  "About Employment - Your Expenses Form" should {
    val jobId = "1"
    val yes = "yes"
    val no = "no"
    val name1 = "Jane Doe"
    val name2 = "John Higgins"
    val noRelation= "No relation"
    val someRelation = "Some other relation"
    val expenses = "some expenses 123"
    val howMuch =  "125.45"
    val howOften_frequency = Other
    val howOften_frequency_other = "Every day and twice on Sundays"


    "map data into case class" in {
      G8AboutExpenses.form.bind(
        Map(
          "jobID" -> jobId,
          "haveExpensesForJob" -> yes,
          "whatExpensesForJob" -> expenses,
          "payAnyoneToLookAfterChildren" -> yes,
          "nameLookAfterChildren" -> name1,
          "howMuchLookAfterChildren" -> howMuch,
          "howOftenLookAfterChildren.frequency" -> howOften_frequency,
          "howOftenLookAfterChildren.frequency.other" -> howOften_frequency_other,
          "relationToYouLookAfterChildren" -> noRelation,
          "relationToPersonLookAfterChildren" -> someRelation,
          "payAnyoneToLookAfterPerson" -> yes,
          "nameLookAfterPerson" -> name2,
          "howMuchLookAfterPerson" -> howMuch,
          "howOftenLookAfterPerson.frequency" -> howOften_frequency,
          "howOftenLookAfterPerson.frequency.other" -> howOften_frequency_other,
          "relationToYouLookAfterPerson" -> noRelation,
          "relationToPersonLookAfterPerson" -> someRelation)
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.jobID must equalTo(jobId)
          f.haveExpensesForJob must equalTo(yes)
          f.whatExpensesForJob must equalTo(Some(expenses))
          f.payAnyoneToLookAfterChildren must equalTo(yes)
          f.nameLookAfterChildren must equalTo(Some(name1))
          f.howMuchLookAfterChildren must equalTo(Some(howMuch))
          f.howOftenLookAfterChildren must equalTo(Some(PensionPaymentFrequency(howOften_frequency, Some(howOften_frequency_other))))
          f.relationToYouLookAfterChildren must equalTo(Some(noRelation))
          f.relationToPersonLookAfterChildren must equalTo(Some(someRelation))
          f.payAnyoneToLookAfterPerson must equalTo(yes)
          f.nameLookAfterPerson must equalTo(Some(name2))
          f.howMuchLookAfterPerson must equalTo(Some(howMuch))
          f.howOftenLookAfterPerson must equalTo(Some(PensionPaymentFrequency(howOften_frequency, Some(howOften_frequency_other))))
          f.relationToYouLookAfterPerson must equalTo(Some(noRelation))
          f.relationToPersonLookAfterPerson must equalTo(Some(someRelation))
        }
      )
    }

    "have 4 mandatory fields" in {
      G8AboutExpenses.form.bind(
        Map("whatExpensesForJob" -> expenses,
            "nameLookAfterChildren" -> name1,
            "howMuchLookAfterChildren" -> howMuch,
            "howOftenLookAfterChildren.frequency" -> howOften_frequency,
            "howOftenLookAfterChildren.frequency.other" -> howOften_frequency_other,
            "relationToYouLookAfterChildren" -> noRelation,
            "relationToPersonLookAfterChildren" -> someRelation,
            "nameLookAfterPerson" -> name2,
            "howMuchLookAfterPerson" -> howMuch,
            "howOftenLookAfterPerson.frequency" -> howOften_frequency,
            "howOftenLookAfterPerson.frequency.other" -> howOften_frequency_other,
            "relationToYouLookAfterPerson" -> noRelation,
            "relationToPersonLookAfterPerson" -> someRelation)
      ).fold(
          formWithErrors => {
            formWithErrors.errors.length must equalTo(4)
            formWithErrors.errors(0).message must equalTo("error.required")
            formWithErrors.errors(1).message must equalTo("error.required")
            formWithErrors.errors(2).message must equalTo("error.required")
            formWithErrors.errors(3).message must equalTo("error.required")
          },
          f => "This mapping should not happen." must equalTo("Valid")
        )
    }

    "reject if haveExpensesForJob is not filled" in {
      G8AboutExpenses.form.bind(
        Map(
          "jobID" -> jobId,
          "payAnyoneToLookAfterChildren" -> no,
          "payAnyoneToLookAfterPerson" -> no)
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject if payAnyoneToLookAfterChildren is not filled" in {
      G8AboutExpenses.form.bind(
        Map(
          "jobID" -> jobId,
          "haveExpensesForJob" -> no,
          "payAnyoneToLookAfterPerson" -> no)
      ).fold(
          formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
          f => "This mapping should not happen." must equalTo("Valid")
        )
    }

    "reject if payAnyoneToLookAfterPerson is not filled" in {
      G8AboutExpenses.form.bind(
        Map(
          "jobID" -> jobId,
          "haveExpensesForJob" -> no,
          "payAnyoneToLookAfterChildren" -> no)
      ).fold(
          formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
          f => "This mapping should not happen." must equalTo("Valid")
        )
    }

    "have 1 expanded mandatory field if haveExpensesForJob is yes" in {
      G8AboutExpenses.form.bind(
        Map(
          "jobID" -> jobId,
          "haveExpensesForJob" -> yes,
          "payAnyoneToLookAfterChildren" -> no,
          "payAnyoneToLookAfterPerson" -> no)
      ).fold(

          formWithErrors =>  {
            formWithErrors.errors.length must equalTo(1)
            formWithErrors.errors(0).message must equalTo("whatExpensesForJob.required")},
          f => "This mapping should not happen." must equalTo("Valid")
        )
    }

    "have 4 expanded mandatory fields if payAnyoneToLookAfterChildren is yes" in {
      G8AboutExpenses.form.bind(
        Map(
          "jobID" -> jobId,
          "haveExpensesForJob" -> no,
          "payAnyoneToLookAfterChildren" -> yes,
          "payAnyoneToLookAfterPerson" -> no)
      ).fold(
          formWithErrors => {
            formWithErrors.errors.length must equalTo(5)
            formWithErrors.errors(0).message must equalTo("nameLookAfterChildren.required")
            formWithErrors.errors(1).message must equalTo("howMuchLookAfterChildren.required")
            formWithErrors.errors(2).message must equalTo("howOftenLookAfterChildren.required")
            formWithErrors.errors(3).message must equalTo("relationToYouLookAfterChildren.required")
            formWithErrors.errors(4).message must equalTo("relationToPersonLookAfterChildren.required")
          },
          f => "This mapping should not happen." must equalTo("Valid")
        )
    }

    "have 4 expanded mandatory fields if payAnyoneToLookAfterPerson is yes" in {
      G8AboutExpenses.form.bind(
        Map(
          "jobID" -> jobId,
          "haveExpensesForJob" -> no,
          "payAnyoneToLookAfterChildren" -> no,
          "payAnyoneToLookAfterPerson" -> yes)
      ).fold(
          formWithErrors => {
            formWithErrors.errors.length must equalTo(5)
            formWithErrors.errors(0).message must equalTo("nameLookAfterPerson.required")
            formWithErrors.errors(1).message must equalTo("howMuchLookAfterPerson.required")
            formWithErrors.errors(2).message must equalTo("howOftenLookAfterPerson.required")
            formWithErrors.errors(3).message must equalTo("relationToYouLookAfterPerson.required")
            formWithErrors.errors(4).message must equalTo("relationToPersonLookAfterPerson.required")
          },
          f => "This mapping should not happen." must equalTo("Valid")
        )
    }

    "reject if other is select but not filled under look after children" in {
      G8AboutExpenses.form.bind(
        Map("whatExpensesForJob" -> expenses,
          "nameLookAfterChildren" -> name1,
          "howMuchLookAfterChildren" -> howMuch,
          "howOftenLookAfterChildren.frequency" -> howOften_frequency,
          "relationToYouLookAfterChildren" -> noRelation,
          "relationToPersonLookAfterChildren" -> someRelation,
          "nameLookAfterPerson" -> name2,
          "howMuchLookAfterPerson" -> howMuch,
          "howOftenLookAfterPerson.frequency" -> howOften_frequency,
          "howOftenLookAfterPerson.frequency.other" -> howOften_frequency_other,
          "relationToYouLookAfterPerson" -> noRelation,
          "relationToPersonLookAfterPerson" -> someRelation)
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject if other is select but not filled under look after person" in {
      G8AboutExpenses.form.bind(
        Map("whatExpensesForJob" -> expenses,
          "nameLookAfterChildren" -> name1,
          "howMuchLookAfterChildren" -> howMuch,
          "howOftenLookAfterChildren.frequency" -> howOften_frequency,
          "howOftenLookAfterChildren.frequency.other" -> howOften_frequency_other,
          "relationToYouLookAfterChildren" -> noRelation,
          "relationToPersonLookAfterChildren" -> someRelation,
          "nameLookAfterPerson" -> name2,
          "howMuchLookAfterPerson" -> howMuch,
          "howOftenLookAfterPerson.frequency" -> howOften_frequency,
          "relationToYouLookAfterPerson" -> noRelation,
          "relationToPersonLookAfterPerson" -> someRelation)
      ).fold(
          formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
          f => "This mapping should not happen." must equalTo("Valid")
        )
    }
  } section("unit", models.domain.SelfEmployment.id)
}