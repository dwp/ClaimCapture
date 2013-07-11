package controllers.s5_time_spent_abroad

import play.api.mvc.Controller
import utils.helpers.CarersForm._
import models.view.CachedClaim
import controllers.Routing
import play.api.data.Form
import play.api.data.Forms._
import controllers.Mappings._
import models.domain.{AbroadForMoreThan52Weeks, AbroadForMoreThan4Weeks, Claim, Trips}
import models.yesNo.YesNo

object G3AbroadForMoreThan52Weeks extends Controller with Routing with CachedClaim {
  override val route = AbroadForMoreThan52Weeks.id -> routes.G3AbroadForMoreThan52Weeks.present

  val form = Form(
    mapping(
      "anyTrips" -> nonEmptyText.verifying(validYesNo)
    )(YesNo.apply)(YesNo.unapply))

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(Trips)

  def present = claiming { implicit claim => implicit request =>
    val trips = claim.questionGroup(Trips) match {
      case Some(ts: Trips) => ts
      case _ => Trips()
    }

    Ok(views.html.s5_time_spent_abroad.g3_abroad_for_more_than_52_weeks(form, trips, completedQuestionGroups))
  }

  def submit = claiming { implicit claim => implicit request =>
    val trips = claim.questionGroup(Trips) match {
      case Some(ts: Trips) => ts
      case _ => Trips()
    }

    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s5_time_spent_abroad.g3_abroad_for_more_than_52_weeks(formWithErrors, trips, completedQuestionGroups)),
      abroadForMoreThan52Weeks => abroadForMoreThan52Weeks.answer match {
        case "yes" if trips.fiftyTwoWeeksTrips.size < 10 => claim.update(trips) -> Redirect(routes.G4Trip.fiftyTwoWeeks())
        case "yes" => claim.update(trips) -> Redirect(routes.G3AbroadForMoreThan52Weeks.present())
        case _ => claim.update(trips) -> Redirect(routes.TimeSpentAbroad.completed())
      })
  }
}