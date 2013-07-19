package controllers.s5_time_spent_abroad

import play.api.mvc.Controller
import utils.helpers.CarersForm._
import models.view.CachedClaim
import play.api.data.Form
import play.api.data.Forms._
import controllers.Mappings._
import models.domain.{AbroadForMoreThan52Weeks, Claim}
import TimeSpentAbroad.trips

object G3AbroadForMoreThan52Weeks extends Controller with CachedClaim {
  val form = Form(
    mapping(
      "call" -> ignored(routes.G3AbroadForMoreThan52Weeks.present()),
      "anyTrips" -> nonEmptyText.verifying(validYesNo)
    )(AbroadForMoreThan52Weeks.apply)(AbroadForMoreThan52Weeks.unapply))

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(AbroadForMoreThan52Weeks)

  def present = claiming { implicit claim => implicit request =>
    Ok(views.html.s5_time_spent_abroad.g3_abroad_for_more_than_52_weeks(form, trips, completedQuestionGroups))
  }

  def submit = claiming { implicit claim => implicit request =>
    def next(abroadForMoreThan52Weeks: AbroadForMoreThan52Weeks) = abroadForMoreThan52Weeks.anyTrips match {
      case `yes` if trips.fiftyTwoWeeksTrips.size < 10 => Redirect(routes.G4Trip.fiftyTwoWeeks())
      case `yes` => Redirect(routes.G3AbroadForMoreThan52Weeks.present())
      case _ => Redirect(routes.G5otherEEAStateOrSwitzerland.present())
    }

    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s5_time_spent_abroad.g3_abroad_for_more_than_52_weeks(formWithErrors, trips, completedQuestionGroups)),
      abroadForMoreThan52Weeks => claim.update(abroadForMoreThan52Weeks).update(trips) -> next(abroadForMoreThan52Weeks))
  }
}