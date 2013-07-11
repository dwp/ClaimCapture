package controllers.s5_time_spent_abroad

import org.specs2.mutable.{Tags, Specification}
import scala.Some

class G1NormalResidenceAndCurrentLocationFormSpec extends Specification with Tags {

  "map data into case class" in {
    G1NormalResidenceAndCurrentLocation.form.bind(
      Map("liveInUK.answer" -> "no", "liveInUK.whereDoYouLive" -> "Italy", "inGBNow" -> "no")
    ).fold(
      formWithErrors => "This mapping should not happen." must equalTo("Error"),
      caseClass => {
        caseClass.whereDoYouLive.answer must equalTo("no")
        caseClass.whereDoYouLive.text must equalTo(Some("Italy"))
        caseClass.inGBNow must equalTo("no")
      }
    )
  }

  """enforce answer to "Do you normally live in the UK, Republic of Ireland, Isle of Man or the Channel Islands?".""" in {
    G1NormalResidenceAndCurrentLocation.form.bind(
      Map("inGBNow.answer" -> "yes")
    ).fold(
      formWithErrors => formWithErrors.errors.head.key must equalTo("liveInUK.answer"),
      caseClass => "This mapping should not happen." must equalTo("Error")
    )
  }

  """reject input having not answered "Are you in Great Britain now?".""" in {
    G1NormalResidenceAndCurrentLocation.form.bind(
      Map("liveInUK.answer" -> "yes")
    ).fold(
      formWithErrors => formWithErrors.errors.head.key must equalTo("inGBNow"),
      caseClass => "This mapping should not happen." must equalTo("Error")
    )
  }

  """reject Not living in the UK but not specified where.""" in {
    G1NormalResidenceAndCurrentLocation.form.bind(
      Map("liveInUK.answer" -> "no", "liveInUK.whereDoYouLive" -> "", "inGBNow" -> "no")
    ).fold(
      formWithErrors => formWithErrors.errors.head.key must equalTo("liveInUK"),
      caseClass => "This mapping should not happen." must equalTo("Error")
    )
  }

  """accept
       "yes" to "Do you normally live in the UK, Republic of Ireland, Isle of Man or the Channel Islands?" and
       "yes" to "Are you in Great Britain now?".""" in {
    G1NormalResidenceAndCurrentLocation.form.bind(
      Map("liveInUK.answer" -> "yes", "inGBNow" -> "yes")
    ).fold(
      formWithErrors => "This mapping should not happen." must equalTo("Error"),
      caseClass => {
        caseClass.whereDoYouLive.answer must equalTo("yes")
        caseClass.inGBNow must equalTo("yes")
      }
    )
  }

  """accept
       "yes" to "Do you normally live in the UK, Republic of Ireland, Isle of Man or the Channel Islands?" and
       "no" to "Are you in Great Britain now?".""" in {
    G1NormalResidenceAndCurrentLocation.form.bind(
      Map("liveInUK.answer" -> "yes", "inGBNow" -> "no")
    ).fold(
      formWithErrors => "This mapping should not happen." must equalTo("Error"),
      caseClass => {
        caseClass.whereDoYouLive.answer must equalTo("yes")
        caseClass.inGBNow must equalTo("no")
      }
    )
  } section "unit"

}
