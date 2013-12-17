package controllers.s2_about_you

import models.view.{CachedClaim, Navigable}
import play.api.mvc.Controller
import controllers.CarersForms._
import play.api.data.Forms._
import controllers.Mappings._
import models.NationalityAndResidence
import models.yesNo.YesNo
import play.api.data.Form

object G3YourNationality extends Controller with CachedClaim with Navigable {
  val resideInUKMapping =
    "resideInUK" -> mapping("answer" -> nonEmptyText.verifying(validYesNo)
  )(YesNo.apply)(YesNo.unapply)

  val form = Form(mapping(
    "nationalityAndResidence" -> mapping(
      "nationality" -> carersText(maxLength = 35),
      resideInUKMapping,
      "residence" -> optional(carersText(maxLength = 35))
    )(NationalityAndResidence.apply)(NationalityAndResidence.unapply))
}
