package controllers

import play.api.mvc.Controller
import models.claim._
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

  val hasBreaksForm = Form(
    mapping(
      "answer" -> nonEmptyText
    )(HasBreaks.apply)(HasBreaks.unapply))

  val breakInCareForm = Form(
    mapping(
      "moreBreaks" -> nonEmptyText,
      "break" -> optional(mapping(
        "start" -> date.verifying(validDate),
        "end" -> date.verifying(validDate)
      )(Break.apply)(Break.unapply))
    )(BreakInCare.apply)(BreakInCare.unapply))

  def theirPersonalDetailsSubmit = claiming {
    implicit claim => implicit request =>
      theirPersonalDetailsForm.bindFromRequest.fold(
        formWithErrors => BadRequest(""),
        inputForm => claim.update(inputForm) -> Ok(""))
  }

  def hasBreaks = claiming {
    implicit claim => implicit request =>

      /*claim.form(models.claim.ClaimDate.id) match {
        case Some(n) => Ok(views.html.s4_careYouProvide.g9_breaks(breaksForm))
        case _ => Redirect(routes.CarersAllowance.benefits())
      }*/

      Ok(views.html.s4_careYouProvide.g10_hasBreaks(hasBreaksForm))
  }

  def hasBreaksSubmit = claiming {
    implicit claim => implicit request =>
      hasBreaksForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.s4_careYouProvide.g10_hasBreaks(formWithErrors)),
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
      breakInCareForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.s4_careYouProvide.g11_breaksInCare(formWithErrors)),
        inputForm => {
          val breaksInCare = claim.form(BreaksInCare.id) match {
            case Some(b: BreaksInCare) if inputForm.break.isDefined => b.update(Break(inputForm.break.get.start, inputForm.break.get.end))
            case Some(b: BreaksInCare) => b
            case _ if inputForm.break.isDefined => BreaksInCare().update(Break(inputForm.break.get.start, inputForm.break.get.end))
            case _ => BreaksInCare()
          }

          if (inputForm.moreBreaks == "yes") claim.update(breaksInCare) -> Redirect(routes.CareYouProvide.breaksInCare())
          else claim.update(breaksInCare) -> Redirect(routes.CareYouProvide.completed())
        })
  }

  def completed = claiming {
    implicit claim => implicit request =>
      Ok("")
  }
}