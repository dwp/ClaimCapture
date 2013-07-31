package controllers.s9_self_employment

import language.reflectiveCalls
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import controllers.Mappings._
import models.domain.{AboutExpenses, SelfEmploymentPensionsAndExpenses, Claim}
import models.view.CachedClaim
import utils.helpers.CarersForm._
import models.yesNo.YesNoWithText
import controllers.s7_employment.Employment._
import play.api.data.FormError


object G4SelfEmploymentPensionsAndExpenses extends Controller with CachedClaim {
  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(SelfEmploymentPensionsAndExpenses)


  val pensionSchemeMapping =
      "doYouPayToPensionScheme" -> mapping(
        "answer" -> nonEmptyText.verifying(validYesNo),
        "howMuchDidYouPay" -> optional(nonEmptyText verifying validDecimalNumber)
      )(YesNoWithText.apply)(YesNoWithText.unapply)
        .verifying("howMuchDidYouPay", YesNoWithText.validateOnYes _)

  val lookAfterChildrenMapping =
    "doYouPayToLookAfterYourChildren" -> mapping(
      "answer" -> nonEmptyText.verifying(validYesNo),
      "isItTheSameExpenseWhileAtWorkForChildren" -> optional(nonEmptyText.verifying(validYesNo))
    )(YesNoWithText.apply)(YesNoWithText.unapply)
      .verifying("isItTheSameExpenseWhileAtWorkForChildren", YesNoWithText.validateOnYes _)

  val lookAfterCaredForMapping =
    "didYouPayToLookAfterThePersonYouCaredFor" -> mapping(
      "answer" -> nonEmptyText.verifying(validYesNo),
      "isItTheSameExpenseDuringWorkForThePersonYouCaredFor" -> optional(nonEmptyText.verifying(validYesNo))
    )(YesNoWithText.apply)(YesNoWithText.unapply)
      .verifying("isItTheSameExpenseDuringWorkForThePersonYouCaredFor", YesNoWithText.validateOnYes _)

  val form = Form(
    mapping(
      call(routes.G4SelfEmploymentPensionsAndExpenses.present()),
      pensionSchemeMapping,
      lookAfterChildrenMapping,
      lookAfterCaredForMapping
    )(SelfEmploymentPensionsAndExpenses.apply)(SelfEmploymentPensionsAndExpenses.unapply)
  )

  def isExpenseForChildrenToBeDisplayed (implicit claim: Claim) = {
    val yesNoList = jobs.map(j => j.apply(AboutExpenses) match { case Some(n:AboutExpenses) => n.payAnyoneToLookAfterChildren case _ => "no"})
    yesNoList.count(_ == "yes") > 0
  }

  def isExpenseForCaredForToBeDisplayed (implicit claim: Claim) = {
    val yesNoList = jobs.map(j => j.apply(AboutExpenses) match { case Some(n:AboutExpenses) => n.payAnyoneToLookAfterPerson case _ => "no"})
    yesNoList.count(_ == "yes") > 0
  }

  def present = claiming {
    implicit claim => implicit request =>
      whenSectionVisible(Ok(views.html.s9_self_employment.g4_selfEmploymentPensionsAndExpenses(form.fill(SelfEmploymentPensionsAndExpenses), completedQuestionGroups)))
  }

  def submit = claiming {
    implicit claim =>
      implicit request =>
        form.bindEncrypted.fold(
          formWithErrors => {
            val formWithErrorsUpdate = formWithErrors
              .replaceError("doYouPayToPensionScheme", "howMuchDidYouPay", FormError("doYouPayToPensionScheme.howMuchDidYouPay", "error.required"))
              .replaceError("doYouPayToLookAfterYourChildren", "isItTheSameExpenseWhileAtWorkForChildren", FormError("doYouPayToLookAfterYourChildren.isItTheSameExpenseWhileAtWorkForChildren", "error.required"))
              .replaceError("didYouPayToLookAfterThePersonYouCaredFor", "isItTheSameExpenseDuringWorkForThePersonYouCaredFor", FormError("didYouPayToLookAfterThePersonYouCaredFor.isItTheSameExpenseDuringWorkForThePersonYouCaredFor", "error.required"))
            BadRequest(views.html.s9_self_employment.g4_selfEmploymentPensionsAndExpenses(formWithErrorsUpdate, completedQuestionGroups))
          },
          f => claim.update(f) -> Redirect(routes.G5ChildcareExpensesWhileAtWork.present())
        )
  }
}
