package controllers.circs.s2_additional_info

import org.specs2.mutable.{Tags, Specification}
import controllers.circs.s1_identification.G3DetailsOfThePersonYouCareFor

class G1OtherChangeInfoFormSpec extends Specification with Tags {

  val otherInfo = "This is my other info"

  "Change of circumstances - Other Change Info Form" should {
    "map data into case class" in {
      G1OtherChangeInfo.form.bind(
        Map("changeInCircs" -> otherInfo)
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.change must equalTo(otherInfo)
        }
      )
    }
    "fail if no data into case class" in {
      G1OtherChangeInfo.form.bind(
        Map("changeInCircs" -> "")
      ).fold(
        formWithErrors => {
          formWithErrors.errors(0).message must equalTo("error.required")
        },
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject special characters in text fields" in {
      G1OtherChangeInfo.form.bind(
        Map("changeInCircs" -> "<>")
      ).fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(1)
          formWithErrors.errors(0).message must equalTo("error.restricted.characters")
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }


  } section("unit", models.domain.CircumstancesAdditionalInfo.id)

}
