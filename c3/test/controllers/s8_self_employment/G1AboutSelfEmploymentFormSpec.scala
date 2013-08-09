package controllers.s8_self_employment

import org.specs2.mutable.{Tags, Specification}

class G1AboutSelfEmploymentFormSpec extends Specification with Tags {

  "About Self Employment - About Self Employment Form" should {

    "map data into case class" in {
      G1AboutSelfEmployment.form.bind(
        Map("areYouSelfEmployedNow" -> "yes",
          "whenDidYouStartThisJob.day" -> "11",
          "whenDidYouStartThisJob.month" -> "11",
          "whenDidYouStartThisJob.year" -> "2011",
          "whenDidTheJobFinish.day" -> "11",
          "whenDidTheJobFinish.month" -> "11",
          "whenDidTheJobFinish.year" -> "2018",
          "haveYouCeasedTrading" -> "no",
          "natureOfYourBusiness" -> "Consulting")
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.natureOfYourBusiness must equalTo(Some("Consulting"))
        }
      )
    }

    "reject if areYouSelfEmployedNow is not filled" in {
      G1AboutSelfEmployment.form.bind(
        Map("natureOfYourBusiness" -> "Consulting")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "About Self Employment - Allow optional fields to be left blank" in {
      G1AboutSelfEmployment.form.bind(
        Map("areYouSelfEmployedNow" -> "yes")
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.areYouSelfEmployedNow must equalTo("yes")
        }
      )
    }
  } section("unit", models.domain.SelfEmployment.id)
}