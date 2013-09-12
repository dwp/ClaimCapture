package controllers.s5_time_spent_abroad

import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import controllers.Mappings._
import models.domain.AbroadForMoreThan4Weeks
import TimeSpentAbroad.trips
import models.view.Navigable
import utils.helpers.CarersForm._
import models.view.CachedClaim

object G2AbroadForMoreThan4Weeks extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "anyTrips" -> nonEmptyText.verifying(validYesNo)
  )(AbroadForMoreThan4Weeks.apply)(AbroadForMoreThan4Weeks.unapply))

  def present = executeOnForm {implicit claim => implicit request =>
    val `4WeeksForm` = form // [SKW] DE201 the yes/no radio button should be blank, forcing the user to answer Yes or No. This change only affects presentation and not the saving (which is done in the submit).
    track(AbroadForMoreThan4Weeks) { implicit claim => Ok(views.html.s5_time_spent_abroad.g2_abroad_for_more_than_4_weeks(`4WeeksForm`, trips)) }
  }

  def submit = executeOnForm {implicit claim => implicit request =>
    def next(abroadForMoreThan4Weeks: AbroadForMoreThan4Weeks) = abroadForMoreThan4Weeks.anyTrips match {
      case `yes` if trips.fourWeeksTrips.size < 5 => Redirect(routes.G4Trip.fourWeeks())
      case `yes` => Redirect(routes.G2AbroadForMoreThan4Weeks.present())
      case _ => Redirect(routes.G3AbroadForMoreThan52Weeks.present())
    }

    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s5_time_spent_abroad.g2_abroad_for_more_than_4_weeks(formWithErrors, trips)),
      abroadForMoreThan4Weeks => claim.update(abroadForMoreThan4Weeks).update(trips) -> next(abroadForMoreThan4Weeks))
  }
}