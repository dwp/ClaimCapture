package controllers.s12_consent_and_declaration

import language.reflectiveCalls
import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import models.view.CachedClaim
import models.domain._
import utils.helpers.CarersForm._
import models.view.Navigable
import controllers.CarersForms._
import play.api.Logger
import controllers.submission.AsyncSubmissionController
import monitoring.ClaimBotChecking
import services.submission.ClaimSubmissionService
import services.ClaimTransactionComponent
import play.api.data.FormError

class G3Declaration extends Controller with CachedClaim with Navigable
       with AsyncSubmissionController
       with ClaimBotChecking
       with ClaimSubmissionService
       with ClaimTransactionComponent{
  val claimTransaction = new ClaimTransaction

  val form = Form(mapping(
    "confirm" -> carersNonEmptyText,
    "nameOrOrganisation" -> optional(carersNonEmptyText(maxLength = 60)),
    "someoneElse" -> optional(carersText),
    "jsEnabled" -> boolean
  )(Declaration.apply)(Declaration.unapply)
    .verifying("nameOrOrganisation",Declaration.validateNameOrOrganisation _))

  def present = claimingWithCheck { implicit claim => implicit request => implicit lang =>
    track(Declaration) { implicit claim => Ok(views.html.s12_consent_and_declaration.g3_declaration(form.fill(Declaration))) }
  }

  def submit = claiming { implicit claim => implicit request => implicit lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val updatedFormWithErrors = formWithErrors.replaceError("","nameOrOrganisation", FormError("nameOrOrganisation", "error.required"))
        BadRequest(views.html.s12_consent_and_declaration.g3_declaration(updatedFormWithErrors))
      },
      declaration => {
        val updatedClaim = copyInstance(claim.update(declaration))
        Logger.debug(updatedClaim.getClass.toString) // class models.view.CachedClaim$$anon$2
        checkForBot(updatedClaim, request)
        submission(updatedClaim, request, declaration.jsEnabled)
      })
  }
}