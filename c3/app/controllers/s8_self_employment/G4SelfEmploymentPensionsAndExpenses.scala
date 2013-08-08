package controllers.s8_self_employment

import language.reflectiveCalls
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import controllers.Mappings._
import models.domain.{SelfEmploymentPensionsAndExpenses, Claim}
import models.view.CachedClaim
import utils.helpers.CarersForm._
import models.yesNo.YesNoWithText
import controllers.s8_self_employment.SelfEmployment._
import play.api.data.FormError


object G4SelfEmploymentPensionsAndExpenses extends Controller with CachedClaim {
  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(SelfEmploymentPensionsAndExpenses)


  val pensionSchemeMapping =
      "doYouPayToPensionScheme" -> mapping(
        "answer" -> nonEmptyText.verifying(validYesNo),
        "howMuchDidYouPay" -> optional(nonEmptyText verifying validDecimalNumber)
      )(YesNoWithText.apply)(YesNoWithText.unapply)
        .verifying("howMuchDidYouPay", YesNoWithText.validateOnYes _)


  def form(implicit claim: Claim) = Form(
    mapping(
      call(routes.G4SelfEmploymentPensionsAndExpenses.present()),
      pensionSchemeMapping,
      "doYouPayToLookAfterYourChildren" -> nonEmptyText.verifying(validYesNo),
      "didYouPayToLookAfterThePersonYouCaredFor" -> nonEmptyText.verifying(validYesNo)
    )(SelfEmploymentPensionsAndExpenses.apply)(SelfEmploymentPensionsAndExpenses.unapply)
  )

  def present = claiming {
    implicit claim => implicit request =>
      whenSectionVisible(Ok(views.html.s8_self_employment.g4_selfEmploymentPensionsAndExpenses(form.fill(SelfEmploymentPensionsAndExpenses), completedQuestionGroups)))
  }

  def submit = claiming {
    implicit claim =>
      implicit request =>
        form.bindEncrypted.fold(
          formWithErrors => {
            val formWithErrorsUpdate = formWithErrors
              .replaceError("doYouPayToPensionScheme", "howMuchDidYouPay", FormError("doYouPayToPensionScheme.howMuchDidYouPay", "error.required"))
            BadRequest(views.html.s8_self_employment.g4_selfEmploymentPensionsAndExpenses(formWithErrorsUpdate, completedQuestionGroups))
          },
          f => claim.update(f) -> Redirect(routes.G5ChildcareExpensesWhileAtWork.present())
        )
  }
}
