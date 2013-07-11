package controllers.s5_time_spent_abroad

import org.specs2.mutable.{Tags, Specification}
import scala.Some

class G1NormalResidenceAndCurrentLocationFormSpec extends Specification with Tags {

  "map data into case class" in {
    G1NormalResidenceAndCurrentLocation.form.bind(
      Map("liveInUK.answer" -> "no", "liveInUK.whereDoYouLive" -> "Italy", "inGBNow" -> "no")
    ).fold(
      formWithErrors => formWithErrors.errors.head must equalTo("Error"),
      caseClass => {
        caseClass.whereDoYouLive.answer must equalTo("no")
        caseClass.whereDoYouLive.text must equalTo(Some("Italy"))
        caseClass.inGBNow must equalTo("no")
      }
    )
  }

//  """enforce answer to "Do you normally live in the UK, Republic of Ireland, Isle of Man or the Channel Islands?".""" in {
//    G1NormalResidenceAndCurrentLocation.form.bind(
//      Map("liveInUk.whereDoYouLive" -> "Italy")
//    ).fold(
//      formWithErrors => formWithErrors.errors.head.key must equalTo("living"),
//      caseClass => "This mapping should not happen." must equalTo("Error")
//    )
//  }

}
