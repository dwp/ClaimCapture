package controllers.circs.breaks_in_care

import controllers.CarersForms._
import controllers.IterationID
import controllers.mappings.Mappings
import controllers.mappings.Mappings._
import models.domain._
import models.view.{CachedChangeOfCircs, CachedClaim}
import play.api.Play._
import play.api.data.Forms._
import play.api.data.{Form, FormError}
import play.api.i18n.{I18nSupport, MMessages, MessagesApi}
import play.api.mvc.{Controller, Request}
import utils.helpers.CarersForm._
import controllers.CarersForms._

object GBreaksInCareHospital extends Controller with CachedChangeOfCircs with I18nSupport with BreaksGatherChecks {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]

  val yourStayEndedMapping = "yourStayEnded" -> optional(yesNoWithDate)

  val dpStayEndedMapping = "dpStayEnded" -> optional(yesNoWithDate)

  val form = Form(mapping(
    "iterationID" -> carersNonEmptyText,
    "typeOfCare" -> default(carersNonEmptyText, Breaks.hospital),
    "whoWasInHospital" -> carersNonEmptyText.verifying(validWhoWasAwayType),
    "whenWereYouAdmitted" -> optional(dayMonthYear),
    yourStayEndedMapping,
    "whenWasDpAdmitted" -> optional(dayMonthYear),
    dpStayEndedMapping,
    "breaksInCareStillCaring" -> optional(nonEmptyText),
    "yourMedicalProfessional" -> default(optional(nonEmptyText), None),
    "dpMedicalProfessional" -> default(optional(nonEmptyText), None),
    "caringEnded" -> default(optional(dayMonthYear), None),
    "caringStarted" -> default(optional(yesNoWithDate), None),
    "whereWasDp" -> default(optional(radioWithText), None),
    "whereWereYou" -> default(optional(radioWithText), None),
    "whenWereYouAdmitted.time" -> default(optional(carersNonEmptyText), None),
    "caringEnded.time" -> default(optional(carersNonEmptyText), None)
  )(CircsBreak.apply)(CircsBreak.unapply)
    .verifying(requiredWhenWereYouAdmitted)
    .verifying(requiredYourStayEndedAnswer)
    .verifying(requiredYourStayEndedDate)
    .verifying(requiredWhenWasDpAdmitted)
    .verifying(requiredDpStayEndedAnswer)
    .verifying(requiredDpStayEndedDate)
    .verifying(requiredBreaksInCareStillCaring)
    .verifying(requiredStartDateNotAfterEndDate)
  )

  val backCall = routes.GBreaksInCareSummary.present()

  def present(iterationID: String) = claimingWithCheck { implicit circs => implicit request => implicit request2lang =>
    println("Doing HOSPITAL present...")
    val break = circs.questionGroup[CircsBreaksInCare].getOrElse(CircsBreaksInCare(List())).breaks.find(_.iterationID == iterationID).getOrElse(CircsBreak())
    println("Got break:" + break)
    Ok(views.html.circs.breaks_in_care.breaksInCareHospital(form.fill(break), backCall))
  }

  def submit = claimingWithCheck { implicit circs => implicit request => implicit request2lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val dp = circsDpName(circs)
        val formWithErrorsUpdate = formWithErrors
          .replaceError("", "whenWereYouAdmitted", FormError("whenWereYouAdmitted", errorRequired))
          .replaceError("", "whenWereYouAdmitted.invalid", FormError("whenWereYouAdmitted", errorInvalid))
          .replaceError("", "yourStayEnded.answer", FormError("yourStayEnded.answer", errorRequired))
          .replaceError("", "yourStayEnded.date", FormError("yourStayEnded.date", errorRequired))
          .replaceError("", "yourStayEnded.date.invalid", FormError("yourStayEnded.date", errorInvalid))
          .replaceError("", "yourStayEnded.invalidDateRange", FormError("yourStayEnded.date", errorInvalidDateRange))
          .replaceError("", "whenWasDpAdmitted", FormError("whenWasDpAdmitted", errorRequired, Seq(dp)))
          .replaceError("", "whenWasDpAdmitted.invalid", FormError("whenWasDpAdmitted", errorInvalid, Seq(dp)))
          .replaceError("", "dpStayEnded.answer", FormError("dpStayEnded.answer", errorRequired))
          .replaceError("", "dpStayEnded.date", FormError("dpStayEnded.date", errorRequired, Seq(dp)))
          .replaceError("", "dpStayEnded.date.invalid", FormError("dpStayEnded.date", errorInvalid, Seq(dp)))
          .replaceError("", "dpStayEnded.invalidDateRange", FormError("dpStayEnded.date", errorInvalidDateRange, Seq(dp)))
          .replaceError("", "breaksInCareStillCaring", FormError("breaksInCareStillCaring", errorRequired, Seq(dp)))
          .replaceError("", "breaksInCareStillCaring.invalidYesNo", FormError("breaksInCareStillCaring", invalidYesNo, Seq(dp)))
        BadRequest(views.html.circs.breaks_in_care.breaksInCareHospital(formWithErrorsUpdate, backCall))
      },
      break => {
        val updatedBreaksInCare = breaksInCare.update(break).breaks.size match {
          case noOfBreaks if (noOfBreaks > Breaks.maximumBreaks) => breaksInCare
          case _ => breaksInCare.update(break)
        }
        val updatedClaim = updateClaim(updatedBreaksInCare)
        updatedClaim -> Redirect(nextPage)
      })
  }

  private def updateClaim(newbreaks: CircsBreaksInCare)(implicit circs: Claim) = {
    val updatedBreaks = updatedBreakTypesObject
    val updatedClaim = circs.update(updatedBreaks).update(newbreaks)
    updatedClaim
  }

  private def updatedBreakTypesObject(implicit circs: Claim) = {
    // Delete the hospital answer from claim. Otherwise, it will prepopulate the answer when return to Summary page
    // But if we are the last break type being collected, clear all answers so that summary page needs to be re-selected.
    if (breaksTypes.carehome.isDefined || breaksTypes.other.equals(Some(Mappings.yes))) {
      breaksTypes.copy(hospital = None)
    }
    else {
      new BreaksInCareType()
    }
  }

  private def nextPage(implicit circs: Claim, request: Request[_]) = {
    val breaksInCareType = circs.questionGroup(CircsBreaksInCareType).getOrElse(CircsBreaksInCareType()).asInstanceOf[CircsBreaksInCareType]
    (breaksInCareType.carehome.isDefined, breaksInCareType.other.isDefined) match {
      case (true, _) => controllers.circs.breaks_in_care.routes.GBreaksInCareRespite.present(IterationID(form))
      case (_, true) if (breaksInCareType.other.get == Mappings.yes) => controllers.circs.breaks_in_care.routes.GBreaksInCareOther.present(IterationID(form))
      case (_, _) => controllers.circs.breaks_in_care.routes.GBreaksInCareSummary.present()
    }
  }

  def breaksInCare(implicit circs: Claim) = circs.questionGroup[CircsBreaksInCare].getOrElse(CircsBreaksInCare())

  def breaksTypes(implicit circs: Claim) = circs.questionGroup[CircsBreaksInCareType].getOrElse(CircsBreaksInCareType())
}
