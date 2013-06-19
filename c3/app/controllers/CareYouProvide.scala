package controllers

import play.api.mvc.Controller
import models.claim.{BreaksInCare, Breaks, TheirPersonalDetails, CachedClaim}
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.Constraints._

object CareYouProvide extends Controller with CachedClaim with FormMappings {

  val theirPersonalDetailsForm = Form(
    mapping(
      "title" -> nonEmptyText,
      "firstName" -> nonEmptyText,
      "middleName" -> optional(text),
      "surname" -> nonEmptyText,
      "otherNames" -> optional(text),
      "nationalInsuranceNumber" -> optional(Forms.nationalInsuranceNumber.verifying(Forms.validNationalInsuranceNumber)),
      "dateOfBirth" -> date.verifying(validDate),
      "liveAtSameAddress" -> nonEmptyText
    )(TheirPersonalDetails.apply)(TheirPersonalDetails.unapply))

  val breaksForm = Form(
    mapping(
      "breaks" -> nonEmptyText
    )(Breaks.apply)(Breaks.unapply))

  val breaksInCareForm = Form(
    mapping(
      "moreBreaks" -> nonEmptyText
    )(BreaksInCare.apply)(BreaksInCare.unapply))

  def theirPersonalDetailsSubmit = claiming {
    implicit claim => implicit request =>
      theirPersonalDetailsForm.bindFromRequest.fold(
        formWithErrors => BadRequest(""),
        inputForm => claim.update(inputForm) -> Ok(""))
  }

  def breaks = claiming {
    implicit claim => implicit request =>

      /*claim.form(models.claim.ClaimDate.id) match {
        case Some(n) => Ok(views.html.s4_careYouProvide.g9_breaks(breaksForm))
        case _ => Redirect(routes.CarersAllowance.benefits())
      }*/

      Ok(views.html.s4_careYouProvide.g10_breaks(breaksForm))
  }

  def breaksSubmit = claiming {
    implicit claim => implicit request =>
      breaksForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.s4_careYouProvide.g10_breaks(formWithErrors)),
        inputForm =>
          if (inputForm.breaks == "yes") claim.update(inputForm) -> Redirect(routes.CareYouProvide.breaksInCare())
          else claim.update(inputForm) -> Redirect(routes.CareYouProvide.completed()))
  }

  def breaksInCare = claiming {
    implicit claim => implicit request =>
      Ok("")
  }

  def breaksInCareSubmit = claiming {
    implicit claim => implicit request =>
      breaksInCareForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.s4_careYouProvide.g11_breaksInCare(formWithErrors)),
        inputForm =>
          if (inputForm.moreBreaks == "yes") claim.update(inputForm) -> Redirect(routes.CareYouProvide.breaksInCare())
          else claim.update(inputForm) -> Redirect(routes.CareYouProvide.completed()))
  }

  def completed = claiming {
    implicit claim => implicit request =>
      Ok("")
  }
}