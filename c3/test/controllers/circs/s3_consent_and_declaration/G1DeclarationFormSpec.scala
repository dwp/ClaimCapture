package controllers.circs.s3_consent_and_declaration

import org.specs2.mutable.{Tags, Specification}

class G1DeclarationFormSpec extends Specification with Tags {
  val byPost = "By Post"
  val infoAgreement = "yes"
  val why = "Cause i want"
  val confirm = "yes"
  val someOneElse = "yes"
  val nameOrOrganisation = "Tesco"
  val wantsEmailContact = "no"

  val G1Declaration = new G1Declaration

  "Change of circumstances - Declaration Form" should {
    "map data into case class" in {
      G1Declaration.form.bind(
        Map("furtherInfoContact" -> byPost, "obtainInfoAgreement" -> infoAgreement, "obtainInfoWhy" -> why, "confirm" -> confirm, "circsSomeOneElse" -> someOneElse, "nameOrOrganisation" -> nameOrOrganisation,"wantsEmailContact" -> wantsEmailContact)
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.obtainInfoAgreement must equalTo(infoAgreement)
          f.obtainInfoWhy.get must equalTo(why)
          f.confirm must equalTo(confirm)
          f.circsSomeOneElse must equalTo(Some(someOneElse))
          f.nameOrOrganisation must equalTo(Some(nameOrOrganisation))
        }
      )
    }

    "reject special characters in text fields" in {
      G1Declaration.form.bind(
        Map("furtherInfoContact" -> byPost, "obtainInfoAgreement" -> infoAgreement, "obtainInfoWhy" -> "whyé", "confirm" -> confirm, "circsSomeOneElse" -> someOneElse, "nameOrOrganisation" -> nameOrOrganisation,"wantsEmailContact" -> wantsEmailContact)
      ).fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(1)
          formWithErrors.errors(0).message must equalTo("error.restricted.characters")
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject form if name of organisation not filled " in {
      G1Declaration.form.bind(
        Map("furtherInfoContact" -> byPost, "obtainInfoAgreement" -> infoAgreement, "obtainInfoWhy" -> why, "confirm" -> confirm, "circsSomeOneElse" -> someOneElse, "nameOrOrganisation" -> "","wantsEmailContact" -> wantsEmailContact)
      ).fold(
          formWithErrors => {
            formWithErrors.errors.length must equalTo(1)
            formWithErrors.errors(0).message must equalTo("nameOrOrganisation")
          },
          f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject form if name of further information contact not filled " in {
      G1Declaration.form.bind(
        Map("furtherInfoContact" -> "", "obtainInfoAgreement" -> infoAgreement, "obtainInfoWhy" -> why, "confirm" -> confirm, "circsSomeOneElse" -> someOneElse, "nameOrOrganisation" -> nameOrOrganisation,"wantsEmailContact" -> wantsEmailContact)
      ).fold(
          formWithErrors => {
            formWithErrors.errors.length must equalTo(1)
            formWithErrors.errors(0).key must equalTo("furtherInfoContact")
            formWithErrors.errors(0).message must equalTo("error.required")
          },
          f => "This mapping should not happen." must equalTo("Valid"))
    }

  } section("unit", models.domain.CircumstancesConsentAndDeclaration.id)

}
