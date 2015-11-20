package controllers.s_eligibility

import play.api.Play._
import play.api.mvc._
import models.view._
import models.domain._
import play.api.data.Form
import play.api.data.Forms._
import play.api.Logger
import controllers.mappings.Mappings._
import utils.helpers.CarersForm._
import play.api.i18n._

object CarersAllowance extends Controller with CachedClaim with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  val form = Form(mapping(
    "allowedToContinue" -> boolean,
    "answer" -> optional(nonEmptyText.verifying(validYesNo)),
    "jsEnabled" -> boolean
  )(ProceedAnyway.apply)(ProceedAnyway.unapply)
    .verifying(errorRequired, mandatoryChecks _))

  def approve = claiming {implicit claim => implicit request => implicit lang => 
    track(Eligibility) { implicit claim => Ok(views.html.s_eligibility.g_approve(form.fill(ProceedAnyway))) }
  }

  def approveSubmit = claiming {implicit claim => implicit request => implicit lang => 
    Logger.debug(s"Approve submit ${claim.uuid}")
    form.bindEncrypted.fold(
      formWithErrors => {
        Logger.info(s"${claim.key} ${claim.uuid} Form with errors: $formWithErrors")
        BadRequest(views.html.s_eligibility.g_approve(formWithErrors))
      },
      f => {
        f.answer match {
          case true =>
            if (!f.jsEnabled) {
              Logger.info(s"No JS - Start ${claim.key} ${claim.uuid} User-Agent : ${request.headers.get("User-Agent").orNull}")
            }
            claim.update(f) -> Redirect(controllers.s_disclaimer.routes.GDisclaimer.present())
          case _ =>
            if (!f.jsEnabled) {
              Logger.info(s"No JS - Stop ${claim.key} ${claim.uuid} User-Agent : ${request.headers.get("User-Agent").orNull}")
            }
            claim.update(f) -> Redirect("https://www.gov.uk/done/apply-carers-allowance")
        }
      }
    )
  }

  def mandatoryChecks(proceedAnyway: ProceedAnyway) = {
    proceedAnyway.allowedToContinue match {
      case true => true
      case false if proceedAnyway.answerYesNo.isDefined => true
      case _ => false
    }
  }
}
