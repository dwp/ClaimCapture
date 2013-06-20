package controllers

import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import models.view.CachedClaim
import Mappings._
import scala.Some
import models.domain.{HasBreaks, TheirPersonalDetails, BreakInCare, Break, BreaksInCare}


object CareYouProvide extends Controller with CachedClaim  {

    val theirPersonalDetailsForm = Form(
    mapping(
      "title" -> nonEmptyText,
      "firstName" -> nonEmptyText,
      "middleName" -> optional(text),
      "surname" -> nonEmptyText,
      "nationalInsuranceNumber" -> optional(Mappings.nationalInsuranceNumber.verifying(Mappings.validNationalInsuranceNumber)),
      "dateOfBirth" -> Mappings.dayMonthYear.verifying(Mappings.validDate),
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
        "start" -> Mappings.dayMonthYear.verifying(Mappings.validDate),
        "end" -> dayMonthYear.verifying(validDate)
      )(Break.apply)(Break.unapply))
    )(BreakInCare.apply)(BreakInCare.unapply))

  def theirPersonalDetails = claiming {
    implicit claim => implicit request =>

      val theirPersonalDetailsQGForm: Form[TheirPersonalDetails] = claim.questionGroup(TheirPersonalDetails.id) match {
        case Some(t: TheirPersonalDetails) => theirPersonalDetailsForm.fill(t)
        case _ => theirPersonalDetailsForm
      }

      Ok(views.html.s4_careYouProvide.g1_theirPersonalDetails(theirPersonalDetailsQGForm))
  }

  def theirPersonalDetailsSubmit = claiming {
    implicit claim => implicit request =>
      theirPersonalDetailsForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.s4_careYouProvide.g1_theirPersonalDetails(formWithErrors)),
        theirPersonalDetails => claim.update(theirPersonalDetails) -> Redirect(routes.CareYouProvide.theirContactDetails()))
  }

  def theirContactDetails = claiming {
    implicit claim => implicit request =>
      Ok(views.html.s4_careYouProvide.g2_theirContactDetails(theirPersonalDetailsForm))
  }

  def hasBreaks = claiming {
    implicit claim => implicit request =>

      val hasBreaksQGForm = claim.questionGroup(models.domain.HasBreaks.id) match {
        case Some(h: HasBreaks) => hasBreaksForm.fill(h)
        case _ => hasBreaksForm
      }

      Ok(views.html.s4_careYouProvide.g10_hasBreaks(hasBreaksQGForm))
  }

  def hasBreaksSubmit = claiming {
    implicit claim => implicit request =>
      hasBreaksForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.s4_careYouProvide.g10_hasBreaks(formWithErrors)),
        hasBreaks =>
          if (hasBreaks.answer == "yes") claim.update(hasBreaks) -> Redirect(routes.CareYouProvide.breaksInCare())
          else claim.update(hasBreaks) -> Redirect(routes.CareYouProvide.completed()))
  }

  def breaksInCare = claiming {
    implicit claim => implicit request =>
      Ok(views.html.s4_careYouProvide.g11_breaksInCare(breakInCareForm))
  }

  def breaksInCareSubmit = claiming {
    implicit claim => implicit request =>
      breakInCareForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.s4_careYouProvide.g11_breaksInCare(formWithErrors)),
        breakInCare => {
          val breaksInCare = claim.questionGroup(BreaksInCare.id) match {
            case Some(b: BreaksInCare) if breakInCare.break.isDefined => b.update(Break(breakInCare.break.get.start, breakInCare.break.get.end))
            case Some(b: BreaksInCare) => b
            case _ if breakInCare.break.isDefined => BreaksInCare().update(Break(breakInCare.break.get.start, breakInCare.break.get.end))
            case _ => BreaksInCare()
          }

          if (breakInCare.moreBreaks == "yes") claim.update(breaksInCare) -> Redirect(routes.CareYouProvide.breaksInCare())
          else claim.update(breaksInCare) -> Redirect(routes.CareYouProvide.completed())
        })
  }

  def completed = claiming {
    implicit claim => implicit request =>
      Ok("")
  }
}