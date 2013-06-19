package controllers

import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import models.view.CachedClaim
import Forms._
import scala.Some
import models.domain.{HasBreaks, TheirPersonalDetails, BreakInCare, Break, BreaksInCare}


object CareYouProvide extends Controller with CachedClaim  {

    val theirPersonalDetailsForm = Form(
    mapping(
      "title" -> nonEmptyText,
      "firstName" -> nonEmptyText,
      "middleName" -> optional(text),
      "surname" -> nonEmptyText,
      "nationalInsuranceNumber" -> optional(Forms.nationalInsuranceNumber.verifying(Forms.validNationalInsuranceNumber)),
      "dateOfBirth" -> Forms.dayMonthYear.verifying(Forms.validDate),
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
        "start" -> Forms.dayMonthYear.verifying(Forms.validDate),
        "end" -> dayMonthYear.verifying(validDate)
      )(Break.apply)(Break.unapply))
    )(BreakInCare.apply)(BreakInCare.unapply))

  def theirPersonalDetails = claiming {
    implicit claim => implicit request =>

      val theirPersonalDetailsFormParam: Form[TheirPersonalDetails] = claim.form(TheirPersonalDetails.id) match {
        case Some(n: TheirPersonalDetails) => theirPersonalDetailsForm.fill(n)
        case _ => theirPersonalDetailsForm
      }
      Ok(views.html.s4_careYouProvide.g1_theirPersonalDetails(theirPersonalDetailsFormParam))
  }

  def theirPersonalDetailsSubmit = claiming {
    implicit claim => implicit request =>
      theirPersonalDetailsForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.s4_careYouProvide.g1_theirPersonalDetails(formWithErrors)),
        inputForm => claim.update(inputForm) -> Redirect(routes.CareYouProvide.theirContactDetails()))
  }

  def theirContactDetails = claiming {
    implicit claim => implicit request =>
      Ok(views.html.s4_careYouProvide.g2_theirContactDetails(theirPersonalDetailsForm))
  }

  def hasBreaks = claiming {
    implicit claim => implicit request =>

      /*claim.form(models.domain.ClaimDate.id) match {
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