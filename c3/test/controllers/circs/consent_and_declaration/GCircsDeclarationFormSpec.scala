package controllers.circs.consent_and_declaration

import utils.WithApplication
import controllers.mappings.Mappings
import org.specs2.mutable._

class GCircsDeclarationFormSpec extends Specification {
  val infoAgreement = "yes"
  val why = "Cause i want"
  val someOneElse = "yes"
  val nameOrOrganisation = "Tesco"

  section("unit", models.domain.CircumstancesConsentAndDeclaration.id)
  "Change of circumstances - Declaration Form" should {
    "map data into case class" in new WithApplication {
      val GCircsDeclaration = new GCircsDeclaration
      GCircsDeclaration.form.bind(
        Map( "obtainInfoAgreement" -> infoAgreement, "obtainInfoWhy" -> why, "circsSomeOneElse" -> someOneElse, "nameOrOrganisation" -> nameOrOrganisation)
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.obtainInfoAgreement must equalTo(infoAgreement)
          f.obtainInfoWhy.get must equalTo(why)
          f.circsSomeOneElse must equalTo(Some(someOneElse))
          f.nameOrOrganisation must equalTo(Some(nameOrOrganisation))
        }
      )
    }

    "reject special characters in text fields" in new WithApplication {
      val GCircsDeclaration = new GCircsDeclaration
      GCircsDeclaration.form.bind(
        Map("obtainInfoAgreement" -> infoAgreement, "obtainInfoWhy" -> "whyé[]", "circsSomeOneElse" -> someOneElse, "nameOrOrganisation" -> nameOrOrganisation)
      ).fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(1)
          formWithErrors.errors(0).message must equalTo(Mappings.errorRestrictedCharacters)
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject form if name of organisation not filled " in new WithApplication {
      val GCircsDeclaration = new GCircsDeclaration
      GCircsDeclaration.form.bind(
        Map("obtainInfoAgreement" -> infoAgreement, "obtainInfoWhy" -> why, "circsSomeOneElse" -> someOneElse, "nameOrOrganisation" -> "")
      ).fold(
          formWithErrors => {
            formWithErrors.errors.length must equalTo(1)
            formWithErrors.errors(0).message must equalTo("nameOrOrganisation")
          },
          f => "This mapping should not happen." must equalTo("Valid"))
    }
  }
  section("unit", models.domain.CircumstancesConsentAndDeclaration.id)
}
