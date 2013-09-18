package controllers.s4_care_you_provide

import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import controllers.Mappings._
import utils.helpers.CarersForm._
import CareYouProvide.breaksInCare
import models.domain.{BreaksInCare, Break}
import models.view.CachedClaim

object G11Break extends Controller with CachedClaim {
  val form = Form(mapping(
    "breakID" -> nonEmptyText,
    "start" -> (dayMonthYear verifying validDate),
    "end" -> optional(dayMonthYear verifying validDateOnly),
    "whereYou" -> whereabouts.verifying(requiredWhereabouts),
    "wherePerson" -> whereabouts.verifying(requiredWhereabouts),
    "medicalDuringBreak" -> nonEmptyText
  )(Break.apply)(Break.unapply))

  // .verifying("wherePerson.other.required", x _)

  def x(break: Break) = {
    println("===> " + break.wherePerson.location)

    break.wherePerson.location match {
      case "Other" => break.wherePerson.other.isDefined
      case _ => true
    }
  }

  def present = executeOnForm { implicit claim => implicit request =>
    Ok(views.html.s4_care_you_provide.g11_break(form))
  }

  def submit = executeOnForm { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s4_care_you_provide.g11_break(formWithErrors)),
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