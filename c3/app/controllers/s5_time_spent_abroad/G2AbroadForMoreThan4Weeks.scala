package controllers.s5_time_spent_abroad

import play.api.mvc.Controller
import utils.helpers.CarersForm._
import models.view.CachedClaim
import controllers.Routing
import play.api.data.Form
import play.api.data.Forms._
import controllers.Mappings._
import models.YesNo
import models.domain.{Claim, Trips}

object G2AbroadForMoreThan4Weeks extends Controller with Routing with CachedClaim {
  override val route = "TODO" -> routes.G2AbroadForMoreThan4Weeks.present

  val form = Form(
    mapping(
      "answer" -> nonEmptyText.verifying(validYesNo)
    )(YesNo.apply)(YesNo.unapply))

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(Trips)

  def present = claiming { implicit claim => implicit request =>
    val trips = claim.questionGroup(Trips) match {
      case Some(t: Trips) => t
      case _ => Trips()
    }

    Ok(views.html.s5_time_spent_abroad.g2_abroad_for_more_than_4_weeks(form, trips, completedQuestionGroups))
  }

  def submit = claiming { implicit claim => implicit request =>
    val trips = claim.questionGroup(Trips) match {
      case Some(t: Trips) => t
      case _ => Trips()
    }

    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s5_time_spent_abroad.g2_abroad_for_more_than_4_weeks(formWithErrors, trips, completedQuestionGroups)),
      abroadForMoreThan4Weeks => abroadForMoreThan4Weeks.answer match {
        case "yes" if trips.fourWeeksTrips.size < 10 => claim.update(trips) -> Redirect(routes.G4Trip.fourWeeks())
        case "yes" => claim.update(trips) -> Redirect(routes.G2AbroadForMoreThan4Weeks.present())
        case _ => claim.update(trips) -> Redirect(routes.TimeSpentAbroad.completed())
      })
  }
}