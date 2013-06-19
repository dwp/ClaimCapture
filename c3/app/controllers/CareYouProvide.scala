package controllers

import play.api.mvc.Controller
import models.claim.{Breaks, TheirPersonalDetails, CachedClaim}
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
      "nationalInsuranceNumber" -> optional(text verifying(pattern( """^([a-zA-Z]){2}( )?([0-9]){2}( )?([0-9]){2}( )?([0-9]){2}( )?([a-zA-Z]){1}?$""".r,
        "constraint.nationalInsuranceNumber", "error.nationalInsuranceNumber"), maxLength(10))),
      "dateOfBirth" -> date.verifying(validDate),
      "liveAtSameAddress" -> nonEmptyText
    )(TheirPersonalDetails.apply)(TheirPersonalDetails.unapply))

  val breaksForm = Form(
    mapping(
      "breaks" -> nonEmptyText
    )(Breaks.apply)(Breaks.unapply))

  def theirPersonalDetailsSubmit = claiming {
    implicit claim => implicit request =>
      theirPersonalDetailsForm.bindFromRequest.fold(
        formWithErrors => BadRequest(""),
        inputForm => claim.update(inputForm) -> Ok(""))
  }

  def breaks = claiming {
    implicit claim => implicit request =>
      Ok("")
  }

  def breaksSubmit = claiming {
    implicit claim => implicit request =>
      breaksForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.s4_careYouProvide.g9_breaks(formWithErrors)),
        inputForm =>
          if (inputForm.breaks == "yes") claim.update(inputForm) -> Redirect(routes.CareYouProvide.breaksInCare())
          else claim.update(inputForm) -> Redirect(routes.CareYouProvide.completed()))
  }

  def breaksInCare = claiming {
    implicit claim => implicit request =>
      Ok("")
  }

  def completed = claiming {
    implicit claim => implicit request =>
      Ok("")
  }
}