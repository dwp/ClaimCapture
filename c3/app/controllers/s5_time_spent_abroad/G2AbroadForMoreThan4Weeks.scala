package controllers.s5_time_spent_abroad

import play.api.mvc.Controller
import utils.helpers.CarersForm._
import models.view.CachedClaim
import controllers.Routing
import play.api.data.Form
import play.api.data.Forms._
import controllers.Mappings._
import models.domain.{AbroadForMoreThan4Weeks, Claim}
import models.yesNo.YesNo
import TimeSpentAbroad.trips

object G2AbroadForMoreThan4Weeks extends Controller with Routing with CachedClaim {
  override val route = AbroadForMoreThan4Weeks.id -> routes.G2AbroadForMoreThan4Weeks.present

  val form = Form(
    mapping(
      "anyTrips" -> nonEmptyText.verifying(validYesNo)
    )(YesNo.apply)(YesNo.unapply))

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(AbroadForMoreThan4Weeks)

  def present = claiming { implicit claim => implicit request =>
    Ok(views.html.s5_time_spent_abroad.g2_abroad_for_more_than_4_weeks(form, trips, completedQuestionGroups))
  }

  def submit = claiming { implicit claim => implicit request =>
    def next(abroadForMoreThan4Weeks: YesNo) = abroadForMoreThan4Weeks.answer match {
      case `yes` if trips.fourWeeksTrips.size < 10 => Redirect(routes.G4Trip.fourWeeks())
      case `yes` => Redirect(routes.G2AbroadForMoreThan4Weeks.present())
      case _ => Redirect(routes.G3AbroadForMoreThan52Weeks.present())
    }

    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s5_time_spent_abroad.g2_abroad_for_more_than_4_weeks(formWithErrors, trips, completedQuestionGroups)),
      abroadForMoreThan4Weeks => claim.update(AbroadForMoreThan4Weeks).update(trips) -> next(abroadForMoreThan4Weeks))
  }
}