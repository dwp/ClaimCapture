package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}

class G3RelationshipAndOtherClaimsFormSpec extends Specification with Tags {

  "More About The Person Form" should {

    "map data into case class" in {
      G3RelationshipAndOtherClaims.form.bind(
        Map("relationship" -> "father", "armedForcesPayment" -> "yes")
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        moreAboutThePerson => {
          moreAboutThePerson.relationship must equalTo("father")
          moreAboutThePerson.armedForcesPayment must equalTo("yes")
        }
      )
    }
  } section("unit", models.domain.CareYouProvide.id)
}