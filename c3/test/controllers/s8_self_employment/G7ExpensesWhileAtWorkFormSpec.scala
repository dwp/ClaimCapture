package controllers.s8_self_employment

import org.specs2.mutable.{Tags, Specification}

class G7ExpensesWhileAtWorkFormSpec extends Specification with Tags {
  "Expenses related to the person you care for while at work - Self Employment Form" should {
    val howMuchYouPay = "123"
    val nameOfPerson = "b"
    val whatRelationIsToYou = "c"
    val whatRelationIsTothePersonYouCareFor = "d"
      
    "map data into case class" in {
      G7ExpensesWhileAtWork.form.bind(
        Map("howMuchYouPay" -> howMuchYouPay,
          "nameOfPerson" -> nameOfPerson,
          "whatRelationIsToYou" -> whatRelationIsToYou,
          "whatRelationIsTothePersonYouCareFor" -> whatRelationIsTothePersonYouCareFor)
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.howMuchYouPay must equalTo(howMuchYouPay)
          f.nameOfPerson must equalTo(nameOfPerson)
          f.whatRelationIsToYou must equalTo(whatRelationIsToYou)
          f.whatRelationIsTothePersonYouCareFor must equalTo(whatRelationIsTothePersonYouCareFor)
        }
      )
    }

    "reject if mandatory field is not filled" in {
      G7ExpensesWhileAtWork.form.bind(
        Map("nameOfPerson" -> "")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }
  } section("unit", models.domain.SelfEmployment.id)
}