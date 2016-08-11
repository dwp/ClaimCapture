package controllers.breaks_in_care

import app.ConfigProperties._
import controllers.CarersForms._
import controllers.IterationID
import controllers.mappings.Mappings
import controllers.mappings.Mappings._
import models.domain._
import models.view.CachedClaim
import play.api.Play._
import play.api.data.Forms._
import play.api.data.{Form, FormError}
import play.api.i18n.{I18nSupport, MMessages, MessagesApi}
import play.api.mvc.{Request, Controller}
import utils.helpers.CarersForm._

/**
 * Created by peterwhitehead on 03/08/2016.
 */
object GBreaksInCareRespite extends Controller with CachedClaim with I18nSupport with BreaksGatherChecks {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]

  val yourStayEndedMapping = "yourRespiteStayEnded" -> optional(yesNoWithDate)

  val dpStayEndedMapping = "dpRespiteStayEnded" -> optional(yesNoWithDate)

  val form = Form(mapping(
    "iterationID" -> carersNonEmptyText,
    "typeOfCare" -> default(carersNonEmptyText, Breaks.carehome),
    "whoWasInRespite" -> carersNonEmptyText.verifying(validWhoWasAwayType),
    "whenWereYouAdmitted" -> optional(dayMonthYear),
    yourStayEndedMapping,
    "whenWasDpAdmitted" -> optional(dayMonthYear),
    dpStayEndedMapping,
    "breaksInCareRespiteStillCaring" -> optional(nonEmptyText),
    "yourMedicalProfessional" -> optional(nonEmptyText),
    "dpMedicalProfessional" -> optional(nonEmptyText),
    "caringEnded" -> default(optional(dayMonthYear), None),
    "caringStarted" -> default(optional(yesNoWithDate), None),
    "whereWasDp" -> default(optional(radioWithText), None),
    "whereWereYou" -> default(optional(radioWithText), None),
    "caringEnded.time" -> default(optional(carersNonEmptyText), None),
    "caringStarted.time" -> default(optional(carersNonEmptyText), None)
  )(Break.apply)(Break.unapply)
    .verifying(requiredWhenWereYouAdmitted)
    .verifying(requiredYourStayEndedAnswer)
    .verifying(requiredYourStayEndedDate)
    .verifying(requiredWhenWasDpAdmitted)
    .verifying(requiredDpStayEndedAnswer)
    .verifying(requiredDpStayEndedDate)
    .verifying(requiredBreaksInCareStillCaring)
    .verifying(requiredStartDateNotAfterEndDate)
    .verifying(requiredMedicalProfessional)
  )

  //need to go back to summary if any breaks exist
  val backCall = routes.GBreaksInCareSummary.present()

  def present(iterationID: String) = claimingWithCheck { implicit claim => implicit request => implicit request2lang =>
    val break = claim.questionGroup[BreaksInCare].getOrElse(BreaksInCare(List())).breaks.find(_.iterationID == iterationID).getOrElse(Break())
    Ok(views.html.breaks_in_care.breaksInCareRespite(form.fill(break), backCall))
  }

  def submit = claimingWithCheck { implicit claim => implicit request => implicit request2lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val theirPersonalDetails = claim.questionGroup(TheirPersonalDetails).getOrElse(TheirPersonalDetails()).asInstanceOf[TheirPersonalDetails]
        val formWithErrorsUpdate = formWithErrors
          .replaceError("", "whenWereYouAdmitted", FormError("whenWereYouAdmitted", errorRequired))
          .replaceError("", "whenWereYouAdmitted.invalid", FormError("whenWereYouAdmitted", errorInvalid))
          .replaceError("", "yourStayEnded.answer", FormError("yourRespiteStayEnded.answer", errorRequired))
          .replaceError("", "yourStayEnded.date", FormError("yourRespiteStayEnded.date", errorRequired))
          .replaceError("", "yourStayEnded.date.invalid", FormError("yourStayEnded.date", errorInvalid))
          .replaceError("", "yourStayEnded.invalidDateRange", FormError("yourStayEnded.date", errorInvalidDateRange))
          .replaceError("", "yourMedicalProfessional", FormError("yourMedicalProfessional", errorRequired))
          .replaceError("", "yourMedicalProfessional.invalidYesNo", FormError("yourMedicalProfessional", invalidYesNo))
          .replaceError("", "whenWasDpAdmitted", FormError("whenWasDpAdmitted", errorRequired, Seq(theirPersonalDetails.firstName + " " + theirPersonalDetails.surname)))
          .replaceError("", "whenWasDpAdmitted.invalid", FormError("whenWasDpAdmitted", errorInvalid, Seq(theirPersonalDetails.firstName + " " + theirPersonalDetails.surname)))
          .replaceError("", "dpStayEnded.answer", FormError("dpRespiteStayEnded.answer", errorRequired))
          .replaceError("", "dpStayEnded.date", FormError("dpRespiteStayEnded.date", errorRequired, Seq(theirPersonalDetails.firstName + " " + theirPersonalDetails.surname)))
          .replaceError("", "dpStayEnded.date.invalid", FormError("dpRespiteStayEnded.date", errorInvalid, Seq(theirPersonalDetails.firstName + " " + theirPersonalDetails.surname)))
          .replaceError("", "dpStayEnded.invalidDateRange", FormError("dpStayEnded.date", errorInvalidDateRange, Seq(theirPersonalDetails.firstName + " " + theirPersonalDetails.surname)))
          .replaceError("", "breaksInCareStillCaring", FormError("breaksInCareRespiteStillCaring", errorRequired, Seq(theirPersonalDetails.firstName + " " + theirPersonalDetails.surname)))
          .replaceError("", "breaksInCareStillCaring.invalidYesNo", FormError("breaksInCareRespiteStillCaring", invalidYesNo, Seq(theirPersonalDetails.firstName + " " + theirPersonalDetails.surname)))
          .replaceError("", "dpMedicalProfessional", FormError("dpMedicalProfessional", errorRequired, Seq(theirPersonalDetails.firstName + " " + theirPersonalDetails.surname)))
          .replaceError("", "dpMedicalProfessional.invalidYesNo", FormError("dpMedicalProfessional", invalidYesNo, Seq(theirPersonalDetails.firstName + " " + theirPersonalDetails.surname)))
        BadRequest(views.html.breaks_in_care.breaksInCareRespite(formWithErrorsUpdate, backCall))
      },
      break => {
        val updatedBreaksInCare = breaksInCare.update(break).breaks.size match {
          case noOfBreaks if (noOfBreaks > getIntProperty("maximumBreaksInCare")) => breaksInCare
          case _ => breaksInCare.update(break)
        }
        val updatedClaim = updateClaim(updatedBreaksInCare)
        updatedClaim -> Redirect(nextPage)
      })
  }

  private def updateClaim(newbreaks: BreaksInCare)(implicit claim: Claim) = {
    // Delete the carehome/respite answer from claim. Otherwise, it will prepopulate the answer when return to Summary page
    // And also update with the new break
    def breaksTypes(implicit claim: Claim) = claim.questionGroup[BreaksInCareType].getOrElse(BreaksInCareType())
    val updatedBreaks = breaksTypes.copy(carehome = None)
    val updatedClaim = claim.update(updatedBreaks).update(newbreaks)
    updatedClaim
  }

  //either other or if come from summary back to there
  private def nextPage(implicit claim: Claim, request: Request[_]) = {
    val breaksInCareType = claim.questionGroup(BreaksInCareType).getOrElse(BreaksInCareType()).asInstanceOf[BreaksInCareType]
    breaksInCareType.other.isDefined match {
      case true if (breaksInCareType.other.get == Mappings.yes) => routes.GBreaksInCareOther.present(IterationID(form))
      case _ => routes.GBreaksInCareSummary.present()
    }
  }

  def breaksInCare(implicit claim: Claim) = claim.questionGroup[BreaksInCare].getOrElse(BreaksInCare())
}
