package controllers.breaks_in_care

import app.BreaksInCareGatherOptions
import app.ConfigProperties._
import controllers.mappings.Mappings
import controllers.mappings.Mappings._
import models.domain._
import models.view.CachedClaim
import models.yesNo.YesNoWithDate
import play.api.Play._
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import play.api.i18n.{MMessages, MessagesApi, I18nSupport}
import play.api.mvc.Controller
import controllers.CarersForms._
import utils.helpers.CarersForm._

/**
  * Created by peterwhitehead on 03/08/2016.
  */
object GBreaksInCareHospital extends Controller with CachedClaim with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]

  val yourStayEnded =
    "yourStayEnded" -> optional(mapping(
      "answer" -> nonEmptyText.verifying(validYesNo),
      "date" -> optional(dayMonthYear.verifying(validDate))
    )(YesNoWithDate.apply)(YesNoWithDate.unapply))

  val dpStayEnded =
    "dpStayEnded" -> optional(mapping(
      "answer" -> nonEmptyText.verifying(validYesNo),
      "date" -> optional(dayMonthYear.verifying(validDate))
    )(YesNoWithDate.apply)(YesNoWithDate.unapply))

  val form = Form(mapping(
    "iterationID" -> carersNonEmptyText,
    "whoWasInHospital" -> carersNonEmptyText.verifying(validWhoWasAwayType),
    "whenWereYouAdmitted" -> optional(dayMonthYear verifying validDate),
    yourStayEnded,
    "whenWasDpAdmitted" -> optional(dayMonthYear verifying validDate),
    dpStayEnded,
    "breaksInCareStillCaring" -> optional(nonEmptyText.verifying(validYesNo))
  )(Break.apply)(Break.unapply)
    .verifying("dpdetails", validateDpDetails _)
    .verifying("youdetails", validateYouDetails _)
  )

  val backCall = routes.GBreakTypes.present()

  def present(iterationID: String) = claimingWithCheck { implicit claim => implicit request => implicit request2lang =>
    //track(BreaksInCare) { implicit claim =>
      val break = claim.questionGroup[BreaksInCare].getOrElse(BreaksInCare(List())).breaks.find(_.iterationID == iterationID).getOrElse(Break())
      Ok(views.html.breaks_in_care.breaksInCareHospital(form.fill(break), backCall))
    //}
  }

  def submit = claiming { implicit claim => implicit request => implicit request2lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val theirPersonalDetails = claim.questionGroup(TheirPersonalDetails).getOrElse(TheirPersonalDetails()).asInstanceOf[TheirPersonalDetails]
        val formWithErrorsUpdate = formWithErrors
          .replaceError("yourStayEnded", "yourStayEnded.date.required", FormError("yourStayEnded.date", errorRequired))
          .replaceError("dpStayEnded", "dpStayEnded.date.required", FormError("dpStayEnded.date", errorRequired))
          .replaceError("breaksInCareStillCaring", errorRequired, FormError("breaksInCareStillCaring", errorRequired, Seq(theirPersonalDetails.firstName + " " + theirPersonalDetails.surname)))
        BadRequest(views.html.breaks_in_care.breaksInCareHospital(formWithErrorsUpdate, backCall))
      },
      break => {
        val updatedBreaksInCare =
          breaksInCare.update(break).breaks.size match {
            case noOfBreaks if (noOfBreaks > getIntProperty("maximumBreaksInCare")) => breaksInCare
            case _ => breaksInCare.update(break)
          }
        // Delete the answer to the question 'Have you had any breaks in care since...'
        // Otherwise, it will prepopulate the answer when asked 'Have you had any more breaks in care since...'
        claim.update(updatedBreaksInCare).delete(BreaksInCareSummary) -> Redirect(routes.GBreakTypes.present())
      })
  }

  private def validateDpDetails(break: Break) = break.whoWasInHospital match {
    case BreaksInCareGatherOptions.You => true
    case _ => validateDp(break)
  }

  private def validateYouDetails(break: Break) = break.whoWasInHospital match {
    case BreaksInCareGatherOptions.DP => true
    case _ => validateYou(break)
  }

  private def validateDp(break: Break) = {
    true
  }

  private def validateYou(break: Break) = {
    true
  }

  def breaksInCare(implicit claim: Claim) = claim.questionGroup[BreaksInCare].getOrElse(BreaksInCare())
}
