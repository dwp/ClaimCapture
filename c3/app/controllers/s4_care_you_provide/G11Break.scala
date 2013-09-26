package controllers.s4_care_you_provide

import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import controllers.Mappings._
import utils.helpers.CarersForm._
import CareYouProvide.breaksInCare
import models.domain.BreaksInCare
import models.view.CachedClaim
import play.api.data.FormError
import models.domain.Break

object G11Break extends Controller with CachedClaim {
  val form = Form(mapping(
    "breakID" -> nonEmptyText,
    "start" -> required(dayMonthYear()),
    "end" -> optional(dayMonthYear()),
    "whereYou" -> whereabouts.verifying(requiredWhereabouts),
    "wherePerson" -> whereabouts.verifying(requiredWhereabouts),
    "medicalDuringBreak" -> nonEmptyText
  )(Break.apply)(Break.unapply))

  def present = executeOnForm { implicit claim => implicit request =>
    Ok(views.html.s4_care_you_provide.g11_break(form))
  }

  def submit = executeOnForm { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val startTimeFormError = FormError("start.time", "error.required")
        val endTimeFormError = FormError("start.time", "error.required")

        val fwe = formWithErrors.replaceError("start.hour", startTimeFormError).replaceError("start.minutes", startTimeFormError)
                                .replaceError("end.hour", endTimeFormError).replaceError("end.minutes", endTimeFormError)

        BadRequest(views.html.s4_care_you_provide.g11_break(fwe))
      },
      break => {
        val updatedBreaksInCare = if (breaksInCare.breaks.size >= 10) breaksInCare else breaksInCare.update(break)
        claim.update(updatedBreaksInCare) -> Redirect(routes.G10BreaksInCare.present())
      })
  }

  def break(id: String) = executeOnForm { implicit claim => implicit request =>
    claim.questionGroup(BreaksInCare) match {
      case Some(b: BreaksInCare) => b.breaks.find(_.id == id) match {
        case Some(b: Break) => Ok(views.html.s4_care_you_provide.g11_break(form.fill(b)))
        case _ => Redirect(routes.G10BreaksInCare.present())
      }

      case _ => Redirect(routes.G10BreaksInCare.present())
    }
  }
}