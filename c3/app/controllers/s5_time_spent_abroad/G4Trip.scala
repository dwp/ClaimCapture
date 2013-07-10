package controllers.s5_time_spent_abroad

import play.api.mvc.Controller
import models.view.CachedClaim
import play.api.data.Form
import play.api.data.Forms._
import controllers.Mappings._
import models.domain.{Trip, FourWeeksTrip, Trips}
import utils.helpers.CarersForm._

object G4Trip extends Controller with CachedClaim {
  val form = Form(
    mapping(
      "tripID" -> nonEmptyText,
      "start" -> (dayMonthYear verifying validDate),
      "end" -> (dayMonthYear verifying validDate),
      "where" -> nonEmptyText,
      "why" -> optional(text)
    )(Trip.apply)(Trip.unapply))

  def fourWeeks = claiming { implicit claim => implicit request =>
    Ok(views.html.s5_time_spent_abroad.g4_trip(form))
  }

  def fourWeeksSubmit = claiming { implicit claim => implicit request =>
    val trips = claim.questionGroup(Trips) match {
      case Some(t: Trips) => t
      case _ => Trips()
    }

    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s5_time_spent_abroad.g4_trip(formWithErrors)),
      trip => {
        val updatedTrips = if (trips.fourWeeksTrips.size >= 10) trips else trips.update(trip.as[FourWeeksTrip])
        claim.update(updatedTrips) -> Redirect(routes.G2AbroadForMoreThan4Weeks.present())
      })
  }

  /*def fiftyTwoWeeks = claiming { implicit claim => implicit request =>
    Ok("")
  }*/
}