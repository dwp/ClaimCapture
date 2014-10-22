package controllers.s2_about_you

import play.api.mvc.Controller
import models.view.CachedClaim
import play.api.data.Form
import play.api.data.Forms._
import models.domain.{AbroadForMoreThan52Weeks, FiftyTwoWeeksTrip, Trips, Trip}
import utils.helpers.CarersForm._
import play.api.i18n.{MMessages => Messages}
import models.DayMonthYear
import AboutYou.trips
import controllers.Mappings._
import controllers.CarersForms._
import scala.Some

object G6Trip extends Controller with CachedClaim {
  val form = Form(mapping(
    "tripID" -> carersNonEmptyText,
    "where" -> carersNonEmptyText(maxLength = 35),
    "start" -> optional(dayMonthYear.verifying(validDateOnly)),
    "end" -> optional(dayMonthYear.verifying(validDateOnly)),
    "why" -> optional(reasonForBeingThere.verifying(requiredReasonForBeingThereOther)),
    "personWithYou" -> nonEmptyText.verifying(validYesNo)
  )(Trip.apply)(Trip.unapply))

  val fiftyTwoWeeksLabel = "s2.g5"

  def present = claimingWithCheck { implicit claim => implicit request => implicit lang =>
    Ok(views.html.s2_about_you.g6_trip(form, fiftyTwoWeeksLabel, routes.G6Trip.submit(), routes.G5AbroadForMoreThan52Weeks.present()))
  }

  def submit = claimingWithCheck { implicit claim => implicit request => implicit lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        BadRequest(views.html.s2_about_you.g6_trip(formWithErrors, fiftyTwoWeeksLabel, routes.G6Trip.submit(), routes.G5AbroadForMoreThan52Weeks.present()))
      },
      trip => {
        val updatedTrips = if (trips.fiftyTwoWeeksTrips.size >= 6) trips else trips.update(trip.as[FiftyTwoWeeksTrip])
        val updatedClaim = claim.update(updatedTrips)
        updatedClaim.delete(AbroadForMoreThan52Weeks) -> Redirect(routes.G5AbroadForMoreThan52Weeks.present())
      })
  }

  def trip(id: String) = claimingWithCheck { implicit claim => implicit request => implicit lang =>
    claim.questionGroup(Trips) match {
      case Some(ts: Trips) => ts.fiftyTwoWeeksTrips.find(_.id == id) match {
        case Some(t: Trip) => Ok(views.html.s2_about_you.g6_trip(form.fill(t), fiftyTwoWeeksLabel, routes.G6Trip.submit(), routes.G5AbroadForMoreThan52Weeks.present()))
        case _ => Redirect(routes.G1YourDetails.present())
      }

      case _ => Redirect(routes.G1YourDetails.present())
    }
  }

  def delete(id: String) = claimingWithCheck { implicit claim => implicit request => implicit lang =>
    import play.api.libs.json.Json
    import language.postfixOps

    claim.questionGroup(Trips) match {
      case Some(ts: Trips) => {
        val updatedTrips = ts.delete(id)
        if (updatedTrips.fiftyTwoWeeksTrips.isEmpty) claim.update(updatedTrips) -> Ok(Json.obj("anyTrips" -> Messages("52Weeks.label", (DayMonthYear.today - 3 years).`dd/MM/yyyy`)))
        else claim.update(updatedTrips) -> Ok(Json.obj("anyTrips" -> Messages("52Weeks.more.label", (DayMonthYear.today - 3 years).`dd/MM/yyyy`)))
      }

      case _ => BadRequest(s"""Failed to delete trip with ID "$id" as claim currently has no trips""")
    }
  }
}
