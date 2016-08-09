package controllers.breaks_in_care

import app.ConfigProperties._
import controllers.CarersForms._
import controllers.IterationID
import controllers.mappings.Mappings._
import models.domain._
import models.view.CachedClaim
import models.yesNo.{RadioWithText, YesNoWithDate}
import play.api.Play._
import play.api.data.Forms._
import play.api.data.{Form, FormError}
import play.api.i18n.{I18nSupport, MMessages, MessagesApi}
import play.api.mvc.{Controller, Request}
import utils.helpers.CarersForm._

/**
  * Created by peterwhitehead on 03/08/2016.
  */
object GBreaksInCareOther extends Controller with CachedClaim with I18nSupport with BreaksGatherChecks {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]

  val whereWasDpMapping = "whereWasDp" -> optional(radioWithText)

  val whereWereYouMapping = "whereWereYou" -> optional(radioWithText)

  val yourStayEndedMapping = "startedCaring" -> optional(yesNoWithDate)

  val form = Form(mapping(
    "iterationID" -> carersNonEmptyText,
    "typeOfCare" -> default(carersNonEmptyText, Breaks.another),
    "whoWasInHospital" -> default(carersNonEmptyText, ""),
    "dpOtherEnded.date" -> optional(dayMonthYear),
    yourStayEndedMapping,
    "whenWasDpAdmitted" -> default(optional(dayMonthYear), None),
    "dpStayEnded" -> default(optional(yesNoWithDate), None),
    "breaksInCareStillCaring" -> default(optional(nonEmptyText), None),
    "yourMedicalProfessional" -> default(optional(nonEmptyText), None),
    "dpMedicalProfessional" -> default(optional(nonEmptyText), None),
    whereWasDpMapping,
    whereWereYouMapping,
    "startedCaring.time" -> optional(carersNonEmptyText),
    "dpOtherEnded.time" -> optional(carersNonEmptyText)
  )(Break.apply)(Break.unapply)

  )

  val backCall = routes.GBreaksInCareType.present()

  def present(iterationID: String) = claimingWithCheck { implicit claim => implicit request => implicit request2lang =>
    val break = claim.questionGroup[BreaksInCare].getOrElse(BreaksInCare(List())).breaks.find(_.iterationID == iterationID).getOrElse(Break())
    Ok(views.html.breaks_in_care.breaksInCareOther(form.fill(break), backCall))
  }

  def submit = claimingWithCheck { implicit claim => implicit request => implicit request2lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val dp = dpDetails(claim);
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
        BadRequest(views.html.breaks_in_care.breaksInCareOther(formWithErrorsUpdate, backCall))
      },
      break => {
        val updatedBreaksInCare =
          breaksInCare.update(break).breaks.size match {
            case noOfBreaks if (noOfBreaks > getIntProperty("maximumBreaksInCare")) => breaksInCare
            case _ => breaksInCare.update(break)
          }
        // Delete the answer to the question 'Have you had any breaks in care since...'
        // Otherwise, it will prepopulate the answer when asked 'Have you had an  y more breaks in care since...'
        val updatedClaim = claim.update(updatedBreaksInCare).delete(BreaksInCareSummary)
        updatedClaim -> Redirect(routes.GBreaksInCareSummary.present())
      })
  }

  def breaksInCare(implicit claim: Claim) = claim.questionGroup[BreaksInCare].getOrElse(BreaksInCare())
}
