package controllers.s_breaks

import controllers.IterationID
import controllers.mappings.Mappings
import controllers.mappings.Mappings._
import models.domain._
import models.view.{CachedClaim, Navigable}
import models.yesNo.DeleteId
import play.api.Logger
import play.api.Play._
import play.api.data.Forms._
import play.api.data.{Form, FormError}
import play.api.mvc.Controller
import utils.helpers.CarersForm._
import utils.helpers.HtmlLabelHelper.displayPlaybackDatesFormat
import scala.language.postfixOps
import play.api.i18n._

object GBreaksInCare extends Controller with CachedClaim with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  val form = Form(mapping(
    "answer" -> nonEmptyText.verifying(validYesNo)
  )(BreaksInCareSummary.apply)(BreaksInCareSummary.unapply))


  def present = claimingWithCheck { implicit claim => implicit request => implicit lang => 
    track(BreaksInCare) { implicit claim => Ok(views.html.s_breaks.g_breaksInCare(form.fill(BreaksInCareSummary), breaksInCare)) }
  }

  def breaksInCare(implicit claim: Claim) = claim.questionGroup[BreaksInCare].getOrElse(BreaksInCare())

  def submit = claimingWithCheck {implicit claim => implicit request => implicit lang => 
    import controllers.mappings.Mappings.yes
    def next(hasBreaks:String) = hasBreaks match {
      case `yes` if breaksInCare.breaks.size < app.ConfigProperties.getProperty("maximumBreaksInCare", 10) => Redirect(controllers.s_breaks.routes.GBreak.present(IterationID(form)))
      case _ => redirect(claim, lang)
    }

    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors.replaceError("answer", Mappings.errorRequired, errorMessage)
        BadRequest(views.html.s_breaks.g_breaksInCare(formWithErrorsUpdate, breaksInCare))
      },
      hasBreaks => claim.update(hasBreaks) -> next(hasBreaks.answer))
  }.withPreviewConditionally[BreaksInCareSummary]((breaksInCareSummary,claims) => breaksInCareSummary._2.answer == Mappings.no)

  private def errorMessage (implicit claim: Claim, lang: Lang, messages: Messages) = {
    val sixMonth = claim.questionGroup(ClaimDate) match {
      case Some(m: ClaimDate) => m.spent35HoursCaringBeforeClaim.answer.toLowerCase == "yes"
      case _ => false
    }
    val breaksAdded = breaksInCare.breaks
    val breaksLabel = if (breaksAdded == Nil || breaksAdded.isEmpty) {
      "answer.label"
    } else {
      "answer.more.label"
    }
    val messageLabel = messages(breaksLabel, claim.dateOfClaim.fold("{NO CLAIM DATE}")(dmy =>
      if (sixMonth) displayPlaybackDatesFormat(lang, dmy - 6 months) else displayPlaybackDatesFormat(lang, dmy)))

    FormError("answer", Mappings.errorRequired, Seq(messageLabel))
  }

  private def redirect(implicit claim: Claim, lang: Lang, messagesApi: MessagesApi) = {
    Redirect(controllers.s_education.routes.GYourCourseDetails.present)
  }

  val deleteForm = Form(mapping(
    "deleteId" -> nonEmptyText
  )(DeleteId.apply)(DeleteId.unapply))

  def delete = claimingWithCheck { implicit claim => implicit request => implicit lang => 


    Logger.info(s"delete, params ${request.body.asFormUrlEncoded}")
    deleteForm.bindEncrypted.fold(
      errors    =>  BadRequest(views.html.s_breaks.g_breaksInCare(form, breaksInCare)),
      deleteForm=>  {
        val updatedBreaks = breaksInCare.delete(deleteForm.id)
        if (updatedBreaks.breaks == breaksInCare.breaks) BadRequest(views.html.s_breaks.g_breaksInCare(form, breaksInCare))
        else claim.update(updatedBreaks).delete(BreaksInCareSummary) -> Redirect(routes.GBreaksInCare.present)
      }
    )
  }

}
