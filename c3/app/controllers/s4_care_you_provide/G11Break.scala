package controllers.s4_care_you_provide

import play.api.mvc.Controller
import play.api.data.Form
import models.view.CachedClaim
import models.domain._
import play.api.data.Forms._
import controllers.Mappings._
import utils.helpers.CarersForm._

object G11Break extends Controller with CachedClaim {
  val form = Form(
    mapping(
      "breakID" -> nonEmptyText,
      "start" -> (dayMonthYear verifying validDate),
      "end" -> optional(dayMonthYear verifying validDate),
      "whereYou" -> whereabouts.verifying(requiredWhereabouts),
      "wherePerson" -> whereabouts.verifying(requiredWhereabouts),
      "medicalDuringBreak" -> nonEmptyText
    )(Break.apply)(Break.unapply))

  def present = claiming { implicit claim => implicit request =>
    Ok(views.html.s4_care_you_provide.g11_break(form))
  }

  def submit = claiming { implicit claim => implicit request =>
    val breaksInCare = claim.questionGroup(BreaksInCare.id) match {
      case Some(b: BreaksInCare) => b
      case _ => BreaksInCare()
    }

    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s4_care_you_provide.g11_break(formWithErrors)),
      break => {
        val updatedBreaksInCare = if (breaksInCare.breaks.size >= 10) breaksInCare else breaksInCare.update(break)
        claim.update(updatedBreaksInCare) -> Redirect(routes.G10BreaksInCare.present)
      })
  }

  def break(id: String) = claiming { implicit claim => implicit request =>
    claim.questionGroup(BreaksInCare.id) match {
      case Some(b: BreaksInCare) => b.breaks.find(_.id == id) match {
        case Some(b: Break) => Ok(views.html.s4_care_you_provide.g11_break(form.fill(b)))
        case _ => Redirect(routes.G10BreaksInCare.present)
      }

      case _ => Redirect(routes.G10BreaksInCare.present)
    }
  }
}