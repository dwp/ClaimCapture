package controllers.s_consent_and_declaration

import language.reflectiveCalls
import play.api.mvc.{Action, AnyContent, Request, Controller}
import play.api.data.{Mapping, Form, FormError}
import play.api.data.Forms.{mapping,optional,nonEmptyText,boolean}
import models.view.CachedClaim
import utils.helpers.CarersForm.formBinding
import models.view.Navigable
import controllers.CarersForms.{carersNonEmptyText, carersText}
import controllers.submission.AsyncSubmissionController
import monitoring.ClaimBotChecking
import services.submission.ClaimSubmissionService
import services.ClaimTransactionComponent
import models.yesNo.{YesNoWithText, OptYesNoWithText}
import models.domain.{AboutOtherMoney, Employment => Emp, Declaration}
import controllers.mappings.Mappings
import controllers.mappings.Mappings.yes

class GDeclaration extends Controller with CachedClaim with Navigable
       with AsyncSubmissionController
       with ClaimBotChecking
       with ClaimSubmissionService
       with ClaimTransactionComponent{
  val claimTransaction = new ClaimTransaction

  val nameOrOrganisation = "nameOrOrganisation"

  val informationFromPersonMapping =
    "tellUsWhyFromAnyoneOnForm" -> mapping(
      "informationFromPerson" -> nonEmptyText,
      "whyPerson" -> optional(carersNonEmptyText(maxLength = 3000)))(YesNoWithText.apply)(YesNoWithText.unapply)
      .verifying(Mappings.required, YesNoWithText.validateOnNo _)

  def form(implicit request: Request[AnyContent]):Form[Declaration] = Form(mapping(
    informationFromPersonMapping,
    nameOrOrganisation -> optional(carersNonEmptyText(maxLength = Mappings.sixty)),
    "someoneElse" -> optional(carersText),
    "jsEnabled" -> boolean
  )(Declaration.apply)(Declaration.unapply)
    .verifying(nameOrOrganisation,Declaration.validateNameOrOrganisation _))

  def present:Action[AnyContent] = claimingWithCheck { implicit claim =>  implicit request =>  lang =>
    track(Declaration) { implicit claim => Ok(views.html.s_consent_and_declaration.g_declaration(form.fill(Declaration))(lang)) }
  }

  def submit:Action[AnyContent] = claiming { implicit claim =>  implicit request =>  lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val updatedFormWithErrors = formWithErrors
          .replaceError("",nameOrOrganisation, FormError(nameOrOrganisation, Mappings.errorRequired))
          .replaceError("tellUsWhyFromAnyoneOnForm", FormError("tellUsWhyFromAnyoneOnForm.whyPerson", Mappings.errorRequired))
        BadRequest(views.html.s_consent_and_declaration.g_declaration(updatedFormWithErrors)(lang))
      },
      declaration => {
        val updatedClaim = copyInstance(claim.update(declaration))
        checkForBot(updatedClaim, request)
        submission(updatedClaim, request, declaration.jsEnabled)
      })
  }
}
