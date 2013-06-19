package controllers

import play.api.mvc.Controller
import models.claim.{TheirPersonalDetails, CachedClaim}
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


  def theirPersonalDetails = claiming {
    implicit claim => implicit request =>

      val theirPersonalDetailsFormParam: Form[TheirPersonalDetails] = claim.form(TheirPersonalDetails.id) match {
        case Some(n: TheirPersonalDetails) => theirPersonalDetailsForm.fill(n)
        case _ => theirPersonalDetailsForm
      }
      Ok(views.html.s4_careyouprovide.g1_theirPersonalDetails(theirPersonalDetailsFormParam))
  }

  def theirPersonalDetailsSubmit = claiming {
    implicit claim => implicit request =>
      theirPersonalDetailsForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.s4_careyouprovide.g1_theirPersonalDetails(formWithErrors)),
        inputForm => claim.update(inputForm) -> Redirect(routes.CareYouProvide.theirContactDetails()))
  }



  def theirContactDetails = claiming {
    implicit claim => implicit request =>
                 /*
      val theirPersonalDetailsFormParam: Form[TheirPersonalDetails] = claim.form(TheirPersonalDetails.id) match {
        case Some(n: TheirPersonalDetails) => theirPersonalDetailsForm.fill(n)
        case _ => theirPersonalDetailsForm
      }
      Ok(views.html.s4_careyouprovide.g1_theirPersonalDetails(theirPersonalDetailsFormParam))  */
      Ok(views.html.s4_careyouprovide.g2_theirContactDetails(theirPersonalDetailsForm))
  }

  def breaks = claiming {
    implicit claim => implicit request =>
      Ok(views.html.s4_careYouProvide.g9_breaks(breaksForm))
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