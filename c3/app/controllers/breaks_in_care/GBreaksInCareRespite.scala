package controllers.breaks_in_care

import app.ConfigProperties._
import controllers.CarersForms._
import controllers.IterationID
import controllers.mappings.Mappings._
import models.domain._
import models.view.CachedClaim
import models.yesNo.YesNoWithDate
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

  val yourStayEndedMapping =
    "yourRespiteStayEnded" -> optional(mapping(
      "answer" -> nonEmptyText,
      "date" -> optional(dayMonthYear)
    )(YesNoWithDate.apply)(YesNoWithDate.unapply))

  val dpStayEndedMapping =
    "dpRespiteStayEnded" -> optional(mapping(
      "answer" -> nonEmptyText,
      "date" -> optional(dayMonthYear)
    )(YesNoWithDate.apply)(YesNoWithDate.unapply))

  val form = Form(mapping(
    "iterationID" -> carersNonEmptyText,
    "typeOfCare" -> default(carersNonEmptyText, "respite"),
    "whoWasInRespite" -> carersNonEmptyText.verifying(validWhoWasAwayType),
    "whenWereYouAdmitted" -> optional(dayMonthYear),
    yourStayEndedMapping,
    "whenWasDpAdmitted" -> optional(dayMonthYear),
    dpStayEndedMapping,
    "breaksInCareRespiteStillCaring" -> optional(nonEmptyText),
    "yourMedicalProfessional" -> optional(nonEmptyText),
    "dpMedicalProfessional" -> optional(nonEmptyText)
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

  val backCall = routes.GBreakTypes.present()

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
        val updatedBreaksInCare =
          breaksInCare.update(break).breaks.size match {
            case noOfBreaks if (noOfBreaks > getIntProperty("maximumBreaksInCare")) => breaksInCare
            case _ => breaksInCare.update(break)
          }
        // Delete the answer to the question 'Have you had any breaks in care since...'
        // Otherwise, it will prepopulate the answer when asked 'Have you had any more breaks in care since...'
        val updatedClaim = claim.update(updatedBreaksInCare).delete(BreaksInCareSummary)
        updatedClaim -> Redirect(nextPage)
      })
  }

  private def nextPage(implicit claim: Claim, request: Request[_]) = {
    val breaksInCareType = claim.questionGroup(BreaksInCareType).getOrElse(BreaksInCareType()).asInstanceOf[BreaksInCareType]
    breaksInCareType.other.isDefined match {
      case true => routes.GBreaksInCareOther.present()
      case _ => routes.GBreaksInCareSummary.present()
    }
  }

  def breaksInCare(implicit claim: Claim) = claim.questionGroup[BreaksInCare].getOrElse(BreaksInCare())
}
