package controllers.s5_time_spent_abroad

import play.api.mvc.Controller
import models.view.CachedClaim
import play.api.data.Form
import play.api.data.Forms._
import models.domain.{FiftyTwoWeeksTrip, Trip, FourWeeksTrip, Trips}
import utils.helpers.CarersForm._
import play.api.i18n.Messages
import models.DayMonthYear
import TimeSpentAbroad.trips
import controllers.Mappings._
import scala.Some

object G4Trip extends Controller with CachedClaim {
  val form = Form(mapping(
    "tripID" -> nonEmptyText,
    "start" -> (dayMonthYear verifying validDate),
    "end" -> (dayMonthYear verifying validDate),
    "where" -> nonEmptyText,
    "why" -> nonEmptyText
  )(Trip.apply)(Trip.unapply))

  def fourWeeks = executeOnForm { implicit claim => implicit request =>
    Ok(views.html.s5_time_spent_abroad.g4_trip(form, routes.G4Trip.fourWeeksSubmit(), routes.G2AbroadForMoreThan4Weeks.present()))
  }

  def fourWeeksSubmit = executeOnForm { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s5_time_spent_abroad.g4_trip(formWithErrors, routes.G4Trip.fourWeeksSubmit(), routes.G2AbroadForMoreThan4Weeks.present())),
      trip => {
        val updatedTrips = if (trips.fourWeeksTrips.size >= 5) trips else trips.update(trip.as[FourWeeksTrip])
        claim.update(updatedTrips) -> Redirect(routes.G2AbroadForMoreThan4Weeks.present())
      })
  }

  def fiftyTwoWeeks = executeOnForm { implicit claim => implicit request =>
    Ok(views.html.s5_time_spent_abroad.g4_trip(form, routes.G4Trip.fiftyTwoWeeksSubmit(), routes.G3AbroadForMoreThan52Weeks.present()))
  }

  def fiftyTwoWeeksSubmit = executeOnForm { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s5_time_spent_abroad.g4_trip(formWithErrors, routes.G4Trip.fiftyTwoWeeksSubmit(), routes.G3AbroadForMoreThan52Weeks.present())),
      trip => {
        val updatedTrips = if (trips.fiftyTwoWeeksTrips.size >= 5) trips else trips.update(trip.as[FiftyTwoWeeksTrip])
        claim.update(updatedTrips) -> Redirect(routes.G3AbroadForMoreThan52Weeks.present())
      })
  }

  def trip(id: String) = executeOnForm { implicit claim => implicit request =>
    claim.questionGroup(Trips) match {
      case Some(ts: Trips) => ts.fourWeeksTrips.find(_.id == id) match {
        case Some(t: Trip) => Ok(views.html.s5_time_spent_abroad.g4_trip(form.fill(t), routes.G4Trip.fourWeeksSubmit(), routes.G2AbroadForMoreThan4Weeks.present()))
        case _ => ts.fiftyTwoWeeksTrips.find(_.id == id) match {
          case Some(t: Trip) => Ok(views.html.s5_time_spent_abroad.g4_trip(form.fill(t), routes.G4Trip.fiftyTwoWeeksSubmit(), routes.G3AbroadForMoreThan52Weeks.present()))
          case _ => Redirect(routes.G1NormalResidenceAndCurrentLocation.present())
        }
      }

      case _ => Redirect(routes.G1NormalResidenceAndCurrentLocation.present())
    }
  }

  def delete(id: String) = executeOnForm { implicit claim => implicit request =>
    import play.api.libs.json.Json
    import language.postfixOps

    claim.questionGroup(Trips) match {
      case Some(ts: Trips) => {
        val updatedTrips = ts.delete(id)

        if (updatedTrips.fourWeeksTrips.isEmpty) claim.update(updatedTrips) -> Ok(Json.obj("anyTrips" -> Messages("4Weeks.label", (DayMonthYear.today - 3 years).`dd/MM/yyyy`)))
        else claim.update(updatedTrips) -> Ok(Json.obj("anyTrips" -> Messages("4Weeks.more.label", (DayMonthYear.today - 3 years).`dd/MM/yyyy`)))
      }

      case _ => BadRequest(s"""Failed to delete trip with ID "$id" as claim currently has no trips""")
    }
  }
}