package controllers.s2_about_you

import controllers.Mappings
import controllers.Mappings._
import controllers.s2_about_you.AboutYou.trips
import models.domain.AbroadForMoreThan52Weeks
import models.view.{CachedClaim, Navigable}
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import utils.helpers.CarersForm._

object G5AbroadForMoreThan52Weeks extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "anyTrips" -> nonEmptyText.verifying(validYesNo)
  )(AbroadForMoreThan52Weeks.apply)(AbroadForMoreThan52Weeks.unapply))

  def present = claimingWithCheck {implicit claim =>  implicit request =>  lang =>
    val filledForm = request.headers.get("referer") match {
      case Some(referer) if referer endsWith routes.G6Trip.present().url => form
      case _ if claim.questionGroup[AbroadForMoreThan52Weeks].isDefined => form.fill(AbroadForMoreThan52Weeks("no"))
      case _ => form
    }

    track(AbroadForMoreThan52Weeks) { implicit claim => Ok(views.html.s2_about_you.g5_abroad_for_more_than_52_weeks(filledForm, trips)(lang)) }
  }

  def submit = claimingWithCheck {implicit claim =>  implicit request =>  lang =>
    def next(abroadForMoreThan52Weeks: AbroadForMoreThan52Weeks) = abroadForMoreThan52Weeks.anyTrips match {
      case `yes` if trips.fiftyTwoWeeksTrips.size < 5 => Redirect(routes.G6Trip.present())
      case `yes` => {
        Redirect(routes.G5AbroadForMoreThan52Weeks.present())
      }
      case _ => Redirect(routes.G7OtherEEAStateOrSwitzerland.present())
    }

    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s2_about_you.g5_abroad_for_more_than_52_weeks(formWithErrors, trips)(lang)),
      abroadForMoreThan52Weeks => claim.update(abroadForMoreThan52Weeks).update(trips) -> next(abroadForMoreThan52Weeks))
  } withPreviewConditionally((abroad:AbroadForMoreThan52Weeks) => abroad.anyTrips == Mappings.no)
}