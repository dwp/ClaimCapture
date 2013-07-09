package controllers.s5_time_spent_abroad

import play.api.mvc.Controller
import models.view.CachedClaim
import play.api.data.Form
import play.api.data.Forms._
import controllers.Mappings._
import models.domain.{FourWeeksTrip, Trips}
import utils.helpers.CarersForm._

object G4Trip extends Controller with CachedClaim {
  val fourWeeksTripForm = Form(
    mapping(
      "tripID" -> nonEmptyText,
      "start" -> (dayMonthYear verifying validDateOnly),
      "end" -> (dayMonthYear verifying validDateOnly),
      "where" -> nonEmptyText,
      "why" -> optional(text)
    )(FourWeeksTrip.apply)(FourWeeksTrip.unapply))

  def fourWeeks = claiming { implicit claim => implicit request =>
    Ok("")
  }

  def fourWeeksSubmit = claiming { implicit claim => implicit request =>
    val trips = claim.questionGroup(Trips) match {
      case Some(t: Trips) => t
      case _ => Trips()
    }

    fourWeeksTripForm.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s5_time_spent_abroad.g4_trip()),
      fourWeeksTrip => {
        val updatedTrips = trips.update(fourWeeksTrip)
        claim.update(updatedTrips) -> Redirect(routes.G2AbroadForMoreThan4Weeks.present())
      })
  }

  /*def fiftyTwoWeeks = claiming { implicit claim => implicit request =>
    Ok("")
  }*/
}


/*
val breaksInCare = claim.questionGroup(BreaksInCare) match {
      case Some(b: BreaksInCare) => b
      case _ => BreaksInCare()
    }

    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s4_care_you_provide.g11_break(formWithErrors)),
      break => {
        val updatedBreaksInCare = if (breaksInCare.breaks.size >= 10) breaksInCare else breaksInCare.update(break)
        claim.update(updatedBreaksInCare) -> Redirect(routes.G10BreaksInCare.present())
      })

*/