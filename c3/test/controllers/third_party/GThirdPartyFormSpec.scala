package controllers.third_party

import controllers.mappings.Mappings
import org.specs2.mutable._
import utils.WithApplication

class GThirdPartyFormSpec extends Specification {
  section ("unit", models.domain.ThirdParty.id)
  "Contact Details Form" should {
    val nameAndOrganisation = "test company 2"
    val nameAndOrganisation61 = "1234567890123456789012345678901234567890123456789012345678901"
    val yesCarer = "yesCarer"
    val noCarer = "noCarer"

    "map data into case class" in new WithApplication {
      GThirdParty.form.bind(
        Map(
          "thirdParty" -> noCarer,
          "thirdParty.nameAndOrganisation" -> nameAndOrganisation
          )).fold(
          formWithErrors => "This mapping should not happen." must equalTo("Error"),
          f => {
            f.nameAndOrganisation must equalTo(Some(nameAndOrganisation))
            f.thirdParty must equalTo(noCarer)
          })
    }

    "reject too many digits in name and organisation field" in new WithApplication {
      GThirdParty.form.bind(
        Map(
          "thirdParty" -> noCarer,
          "thirdParty.nameAndOrganisation" -> nameAndOrganisation61
        )).fold(
          formWithErrors => {
            formWithErrors.errors.length must equalTo(1)
            formWithErrors.errors(0).message must equalTo(Mappings.maxLengthError)
          },
          f => "This mapping should not happen." must equalTo("Valid"))
    }

    "have 1 mandatory fields" in new WithApplication {
      GThirdParty.form.bind(
        Map("thirdParty" -> noCarer)).fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(1)
          formWithErrors.errors(0).message must equalTo("thirdParty.nameAndOrganisation.required")
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }
  }
  section ("unit", models.domain.ThirdParty.id)
}
