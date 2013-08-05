package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}

class G3MoreAboutThePersonFormSpec extends Specification with Tags {

  "More About The Person Form" should {

    "map data into case class" in {
      G3MoreAboutThePerson.form.bind(
        Map("relationship" -> "father", "armedForcesPayment" -> "yes", "claimedAllowanceBefore" -> "no")
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        moreAboutThePerson => {
          moreAboutThePerson.relationship must equalTo("father")
          moreAboutThePerson.armedForcesPayment must equalTo(Some("yes"))
          moreAboutThePerson.claimedAllowanceBefore must equalTo("no")
        }
      )
    }

    "have a mandatory relationship" in {
      G3MoreAboutThePerson.form.bind(
        Map("relationship" -> "", "" -> "", "claimedAllowanceBefore" -> "yes")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
        moreAboutThePerson => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "have a mandatory 'has claimed Carer's Allowance before' question" in {
      G3MoreAboutThePerson.form.bind(
        Map("relationship" -> "father", "" -> "", "claimedAllowanceBefore" -> "")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
        moreAboutThePerson => "This mapping should not happen." must equalTo("Valid")
      )
    }
  } section "unit"

}
