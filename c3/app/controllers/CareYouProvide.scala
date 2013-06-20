package controllers

import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import models.view.CachedClaim
import Mappings._
import scala.Some
import models.domain.{HasBreaks, TheirPersonalDetails, BreakInCare, Break, BreaksInCare}


object CareYouProvide extends Controller with CachedClaim  {
  val route = Map(HasBreaks.id -> routes.CareYouProvide.hasBreaks)

  val theirPersonalDetailsForm = Form(
    mapping(
      "title" -> nonEmptyText,
      "firstName" -> nonEmptyText(maxLength = maxNrOfChars),
      "middleName" -> optional(text(maxLength = maxNrOfChars)),
      "surname" -> nonEmptyText(maxLength = maxNrOfChars),
      "nationalInsuranceNumber" -> optional(nationalInsuranceNumber verifying validNationalInsuranceNumber),
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
        "start" -> (dayMonthYear verifying validDateOnly),
        "end"   -> (dayMonthYear verifying validDateOnly)
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
      val hasBreaksQGForm = claim.questionGroup(HasBreaks.id) match {
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
            case Some(b: BreaksInCare) => breakInCare.break.fold(b)(break => if (b.breaks.size == 10) b else b.update(break))
            case _ => breakInCare.break.fold(BreaksInCare())(BreaksInCare().update)
          }

          breakInCare.moreBreaks match {
            case "no" => claim.update(breaksInCare) -> Redirect(routes.CareYouProvide.completed())
            case "yes" if breaksInCare.breaks.size == 10 => claim.update(breaksInCare) -> Redirect(routes.CareYouProvide.completed(/* TODO WARNING FEEDBACK MESSAGE*/))
            case _ => claim.update(breaksInCare) -> Redirect(routes.CareYouProvide.breaksInCare())
          }
        })
  }

  def completed = claiming {
    implicit claim => implicit request =>
      val outcome =
        <html>
          <head>
            <title>Completed - Care You Provide</title>
          </head>

          <body>
            <h1>End of Sprint 2</h1>

            <ul>
              {claim.completedQuestionGroups(models.domain.CareYouProvide.id).map(f => <li>{f}</li>)}
            </ul>
          </body>
        </html>

      Ok(outcome)
  }
}