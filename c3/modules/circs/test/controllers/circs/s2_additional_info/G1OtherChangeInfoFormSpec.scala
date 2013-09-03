package controllers.circs.s2_additional_info

import org.specs2.mutable.{Tags, Specification}

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
  } section("unit", models.domain.CircumstancesAdditionalInfo.id)

}
