package controllers.s11_consent_and_declaration

import language.reflectiveCalls
import play.api.mvc.{Action, AnyContent, Request, Controller}
import play.api.data.{Mapping, Form, FormError}
import play.api.data.Forms.{mapping,optional,nonEmptyText,boolean}
import models.view.CachedClaim
import utils.helpers.CarersForm.formBinding
import models.view.Navigable
import controllers.CarersForms.{carersNonEmptyText, carersText}
import play.api.Logger
import controllers.submission.AsyncSubmissionController
import monitoring.ClaimBotChecking
import services.submission.ClaimSubmissionService
import services.ClaimTransactionComponent
import models.yesNo.{YesNoWithText, OptYesNoWithText}
import scala.Some
import models.domain.{AboutOtherMoney, Employment => Emp, Declaration}
import controllers.Mappings
import controllers.Mappings.{yes, threeHundred, sixty}

class G4Declaration extends Controller with CachedClaim with Navigable
       with AsyncSubmissionController
       with ClaimBotChecking
       with ClaimSubmissionService
       with ClaimTransactionComponent{
  val claimTransaction = new ClaimTransaction

  val nameOrOrganisation = "nameOrOrganisation"
  val gettingInformationFromAnyEmployer = "gettingInformationFromAnyEmployer"


  def validateEmpRequired(input: OptYesNoWithText)(implicit request: Request[AnyContent]): Boolean = {
    fromCache(request) match {
      case Some(claim) if claim.questionGroup[Emp].getOrElse(Emp()).beenEmployedSince6MonthsBeforeClaim == `yes` ||
        claim.questionGroup[AboutOtherMoney].getOrElse(AboutOtherMoney()).statutorySickPay.answer == `yes` ||
        claim.questionGroup[AboutOtherMoney].getOrElse(AboutOtherMoney()).otherStatutoryPay.answer == `yes`=>
        input.answer.isDefined
      case _ => true
    }
  }

  def informationFromEmployerMapping(implicit request: Request[AnyContent]):(String, Mapping[OptYesNoWithText]) =
    gettingInformationFromAnyEmployer -> mapping(
      "informationFromEmployer" -> optional(carersNonEmptyText),
      "why" -> optional(carersNonEmptyText(maxLength = Mappings.threeHundred)))(OptYesNoWithText.apply)(OptYesNoWithText.unapply)
      .verifying("employerRequired", validateEmpRequired _)
      .verifying(Mappings.required, OptYesNoWithText.validateOnNo _)


  val informationFromPersonMapping =
    "tellUsWhyEmployer" -> mapping(
      "informationFromPerson" -> nonEmptyText,
      "whyPerson" -> optional(carersNonEmptyText(maxLength = Mappings.threeHundred)))(YesNoWithText.apply)(YesNoWithText.unapply)
      .verifying(Mappings.required, YesNoWithText.validateOnNo _)

  def form(implicit request: Request[AnyContent]):Form[Declaration] = Form(mapping(
     informationFromEmployerMapping,
     informationFromPersonMapping,
    "confirm" -> carersNonEmptyText,
    nameOrOrganisation -> optional(carersNonEmptyText(maxLength = Mappings.sixty)),
    "someoneElse" -> optional(carersText),
    "jsEnabled" -> boolean
  )(Declaration.apply)(Declaration.unapply)
    .verifying(nameOrOrganisation,Declaration.validateNameOrOrganisation _))

  def present:Action[AnyContent] = claimingWithCheck { implicit claim => implicit request => implicit lang =>
    track(Declaration) { implicit claim => Ok(views.html.s11_consent_and_declaration.g4_declaration(form.fill(Declaration))) }
  }

  def submit:Action[AnyContent] = claiming { implicit claim => implicit request => implicit lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val updatedFormWithErrors = formWithErrors
          .replaceError("",nameOrOrganisation, FormError(nameOrOrganisation, Mappings.errorRequired))
          .replaceError(gettingInformationFromAnyEmployer,"employerRequired",
                        FormError(gettingInformationFromAnyEmployer.concat(".informationFromEmployer"), Mappings.errorRequired))
          .replaceError(gettingInformationFromAnyEmployer,Mappings.required,
                        FormError(gettingInformationFromAnyEmployer.concat(".why"), Mappings.errorRequired))
          .replaceError("tellUsWhyEmployer", FormError("tellUsWhyEmployer.whyPerson", Mappings.errorRequired))
        BadRequest(views.html.s11_consent_and_declaration.g4_declaration(updatedFormWithErrors))
      },
      declaration => {
        val updatedClaim = copyInstance(claim.update(declaration))
        Logger.debug(updatedClaim.getClass.toString) // class models.view.CachedClaim$$anon$2
        checkForBot(updatedClaim, request)
        submission(updatedClaim, request, declaration.jsEnabled)
      })
  }
}
