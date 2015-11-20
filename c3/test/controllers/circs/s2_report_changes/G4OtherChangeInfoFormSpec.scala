package controllers.circs.s2_report_changes

import utils.WithApplication
import controllers.mappings.Mappings
import org.specs2.mutable._

class G4OtherChangeInfoFormSpec extends Specification {

  val otherInfo = "This is my other info"

  "Change of circumstances - Other Change Info Form" should {
    "map data into case class" in new WithApplication {
      G4OtherChangeInfo.form.bind(
        Map("changeInCircs" -> otherInfo)
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.change must equalTo(otherInfo)
        }
      )
    }
    "fail if no data into case class" in new WithApplication {
      G4OtherChangeInfo.form.bind(
        Map("changeInCircs" -> "")
      ).fold(
        formWithErrors => {
          formWithErrors.errors(0).message must equalTo(Mappings.errorRequired)
        },
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject special characters in text fields" in new WithApplication {
      G4OtherChangeInfo.form.bind(
        Map("changeInCircs" -> "<>")
      ).fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(1)
          formWithErrors.errors(0).message must equalTo(Mappings.errorRestrictedCharacters)
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }


  }
  section("unit", models.domain.CircumstancesReportChanges.id)

}
