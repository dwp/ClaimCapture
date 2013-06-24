package integration.s4_careYouProvide

import org.specs2.mutable.Specification
import controllers.CareYouProvide

class G3MoreAboutThePersonFormSpec extends Specification {

  "More About The Person Form" should {

    "map data into case class" in {
      CareYouProvide.moreAboutThePersonForm.bind(
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
      CareYouProvide.theirContactDetailsForm.bind(
        Map("relationship" -> "", "" -> "", "claimedAllowanceBefore" -> "yes")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
        theirContactDetails => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "have a mandatory 'has claimed Carer's Allowance before' question" in {
      CareYouProvide.theirContactDetailsForm.bind(
        Map("relationship" -> "father", "" -> "", "claimedAllowanceBefore" -> "")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
        theirContactDetails => "This mapping should not happen." must equalTo("Valid")
      )
    }
  }

}
