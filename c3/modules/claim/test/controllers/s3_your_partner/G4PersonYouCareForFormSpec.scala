package controllers.s3_your_partner

import org.specs2.mutable.{Tags, Specification}

class G4PersonYouCareForFormSpec extends Specification with Tags {

  "Person you care for" should {

    "map data into case class" in {
      G4PersonYouCareFor.form.bind(
        Map("isPartnerPersonYouCareFor" -> "yes")
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => f.isPartnerPersonYouCareFor must equalTo("yes")
      )
    }

    "have a mandatory 'Is your partner the Person you care for' " in {
      G4PersonYouCareFor.form.bind(
        Map("isPartnerPersonYouCareFor" -> "")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
        f => "This mapping should not happen." must equalTo("Should not happen")
      )
    }

    "reject an invalid value for 'Is your partner the Person you care for' " in {
      G4PersonYouCareFor.form.bind(
        Map("isPartnerPersonYouCareFor" -> "INVALID")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("yesNo.invalid"),
        f => "This mapping should not happen." must equalTo("Should not happen")
      )
    }

  } section("unit", models.domain.YourPartner.id)
}