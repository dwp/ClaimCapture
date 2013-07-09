package controllers.s5_time_spent_abroad

import play.api.mvc.Controller
import utils.helpers.CarersForm._
import models.view.CachedClaim
import controllers.Routing
import play.api.data.Form
import play.api.data.Forms._
import controllers.Mappings._
import models.YesNo

object G2AbroadForMoreThan4Weeks extends Controller with Routing with CachedClaim {
  override val route = "TODO" -> routes.G2AbroadForMoreThan4Weeks.present

  val form = Form(
    mapping(
      "answer" -> nonEmptyText.verifying(validYesNo)
    )(YesNo.apply)(YesNo.unapply))

  def present = claiming { implicit claim => implicit request =>
    /*val breaksInCare = claim.questionGroup(BreaksInCare) match {
      case Some(b: BreaksInCare) => b
      case _ => BreaksInCare()
    }

    Ok(views.html.s4_care_you_provide.g10_breaksInCare(form, breaksInCare, completedQuestionGroups, dateOfClaimCheckIfSpent35HoursCaringBeforeClaim(claim)))*/
    Ok(views.html.s5_time_spent_abroad.g2_abroad_for_more_than_4_weeks(form))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s5_time_spent_abroad.g2_abroad_for_more_than_4_weeks(form)),
      abroadForMoreThan4Weeks => abroadForMoreThan4Weeks.answer match {
        case _ => Redirect(routes.G4Trip.fourWeeks())
      })
  }
}