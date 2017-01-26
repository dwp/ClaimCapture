package controllers.circs.breaks_in_care

import controllers.CarersForms._
import controllers.IterationID
import controllers.circs.breaks_in_care.GBreaksInCareOther._
import controllers.mappings.Mappings
import controllers.mappings.Mappings._
import models.domain._
import models.view.{CachedChangeOfCircs, Navigable}
import models.yesNo.{DeleteId}
import play.api.Logger
import play.api.Play._
import play.api.data.Forms._
import play.api.data.{Form, FormError}
import play.api.i18n._
import play.api.mvc.{Request, Controller}
import utils.helpers.CarersForm._

import scala.language.postfixOps

object GBreaksInCareSummary extends Controller with CachedChangeOfCircs with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]

  val backCall = controllers.circs.your_details.routes.GYourDetails.present()

  def form(implicit circs: Claim) = Form(mapping(
    "circs_breaktype_hospital" -> optional(nonEmptyText),
    "circs_breaktype_carehome" -> optional(nonEmptyText),
    "circs_breaktype_none" -> optional(nonEmptyText),
    "circs_breaktype_other" -> optional(text.verifying(validYesNo)),
    "moreAboutChanges" -> optional(carersNonEmptyText(maxLength = 3000))
  )(CircsBreaksInCareType.apply)(CircsBreaksInCareType.unapply)
    .verifying("selectother", validateOther _)
    .verifying("musthavebreak1", validateMustHaveBreak(circs, _))
    .verifying("musthavebreak2", validateMustHaveBreak(circs, _))
    .verifying("deselectnone", validateAnySelected _)
    .verifying("selectone", validateNoneNotallowed _)
    .verifying("toomanybreaks", validateMaxReached(circs, _))
  )

  def breaks(implicit circs: Claim) = circs.questionGroup[CircsBreaksInCare].getOrElse(CircsBreaksInCare())

  def present = claiming { implicit circs => implicit request => implicit request2lang =>
    track(CircsBreaksInCareSummary) {
      implicit circs => Ok(views.html.circs.breaks_in_care.breaksInCareSummary(form.fill(CircsBreaksInCareType), breaks))
    }
  }

  def submit = claiming { implicit circs => implicit request => implicit request2lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val errors = formWithErrors
          .replaceError("", "toomanybreaks", FormError("circs_breaktype", "circs_breaks.toomanybreaks", Seq(Breaks.maximumBreaks)))
          .replaceError("", "musthavebreak1", FormError("circs_breaktype", "circs_breaks.musthavebreak", Seq(anyothertimes, circsDpName(circs))))
          .replaceError("", "musthavebreak2", FormError("circs_breaktype_other", "circs_breaks.musthavebreak", Seq(anyothertimes, circsDpName(circs))))
          .replaceError("", "deselectnone", FormError("circs_breaktype", "circs_breaks.breaktype.deselectnone", Seq(anyothertimes, circsDpName(circs))))
          .replaceError("", "selectone", FormError("circs_breaktype", "circs_breaks.breaktype.selectone", Seq(anyothertimes, circsDpName(circs))))
          .replaceError("", "selectother", FormError("circs_breaktype_other", errorRequired, Seq(circsDpName(circs))))
        BadRequest(views.html.circs.breaks_in_care.breaksInCareSummary(errors, breaks))
      },
      breaksInCareSummary => {
        circs.update(breaksInCareSummary) -> Redirect(nextPage(breaksInCareSummary))
      })
  }

  private def otherbreakLabel(implicit claim: Claim) = breaks.hasCircsBreaks match {
    case false => "breaktype_other_first"
    case true => "breaktype_other_another"
  }

  private def nextPage(breaksInCareType: CircsBreaksInCareType)(implicit claim: Claim, request: Request[_]): String = {
    if (breaksInCareType.hospital.isDefined) controllers.circs.breaks_in_care.routes.GBreaksInCareHospital.present(IterationID(form)).url
    else if (breaksInCareType.carehome.isDefined) controllers.circs.breaks_in_care.routes.GBreaksInCareRespite.present(IterationID(form)).url
    else if (breaksInCareType.other.equals(Some(Mappings.yes))) controllers.circs.breaks_in_care.routes.GBreaksInCareOther.present(IterationID(form)).url
    else controllers.circs.consent_and_declaration.routes.GCircsDeclaration.present.url
  }

  private def anyothertimes()(implicit claim: Claim) = (breaks.hasCircsBreaksForType(Breaks.hospital), breaks.hasCircsBreaksForType(Breaks.carehome)) match {
    case (true, _) => Messages("breaktype.anyothertimes")
    case (_, true) => Messages("breaktype.anyothertimes")
    case _ => Messages("breaktype.anytimes")
  }

  private def dateForBreaks(claim: Claim, lang: Lang) = {
    val claimDateQG = claim.questionGroup[ClaimDate].getOrElse(ClaimDate())
    claimDateQG.dateWeRequireBreakInCareInformationFrom(lang)
  }

  private def validateAnySelected(breaksInCareType: CircsBreaksInCareType) = breaksInCareType match {
    case CircsBreaksInCareType(None, None, None, _, _) => false
    case _ => true
  }

  private def validateNoneNotallowed(breaksInCareType: CircsBreaksInCareType) = someTrue match {
    case (breaksInCareType.hospital | breaksInCareType.carehome) if (breaksInCareType.none == someTrue) => false
    case _ => true
  }

  private def validateMustHaveBreak(implicit claim: Claim, breaksInCareType: CircsBreaksInCareType) = {
    (breaksInCare.hasCircsBreaks, breaksInCareType) match {
      case (false, CircsBreaksInCareType(None, None, None, _, _)) => true
      case (false, CircsBreaksInCareType(None, None, someTrue, Some(Mappings.no), _)) => false
      case _ => true
    }
  }

  private def validateOther(breaksInCareType: CircsBreaksInCareType) = breaksInCareType.other match {
    case Some(Mappings.yes) => true
    case Some(Mappings.no) => true
    case _ => false
  }

  private def validateMaxReached(implicit claim: Claim, breaksInCareType: CircsBreaksInCareType) = {
    Logger.info("Validating breaksInCare Maximum with " + breaksInCare.breaks.size + " against maximum of " + Breaks.maximumBreaks)
    (breaksInCare.circsMaximumReached, breaksInCareType.none, breaksInCareType.other) match {
      case (false, _, _) => true
      case (true, Mappings.someTrue, Some(Mappings.no)) => true
      case _ => false
    }
  }

  val deleteForm = Form(mapping(
    "deleteId" -> nonEmptyText
  )(DeleteId.apply)(DeleteId.unapply))

  def delete = claimingWithCheck {
    implicit circs => implicit request => implicit request2lang =>
      deleteForm.bindEncrypted.fold(
        errors => BadRequest(views.html.circs.breaks_in_care.breaksInCareSummary(form.fill(CircsBreaksInCareType), breaks)),
        deleteForm => {
          val updatedBreaks = breaks.delete(deleteForm.id)
          if (updatedBreaks.breaks == breaks.breaks) BadRequest(views.html.circs.breaks_in_care.breaksInCareSummary(form.fill(CircsBreaksInCareSummary), breaks))
          else circs.update(updatedBreaks).delete(BreaksInCareSummary) -> Redirect(routes.GBreaksInCareSummary.present)
        }
      )

  }

  def breaksInCare(implicit claim: Claim) = claim.questionGroup[CircsBreaksInCare].getOrElse(CircsBreaksInCare())
}
