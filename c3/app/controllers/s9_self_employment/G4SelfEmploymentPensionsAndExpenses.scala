package controllers.s9_self_employment

import language.reflectiveCalls
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import controllers.Mappings._
import models.domain.{SelfEmploymentPensionsAndExpenses, Claim}
import models.view.CachedClaim
import utils.helpers.CarersForm._


object G4SelfEmploymentPensionsAndExpenses extends Controller with CachedClaim {
  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(SelfEmploymentPensionsAndExpenses)

  val form = Form(
    mapping(
      call(routes.G4SelfEmploymentPensionsAndExpenses.present()),
      "doYouPayToPensionScheme" -> nonEmptyText.verifying(validYesNo),
      "howMuchDidYouPay" -> optional(text verifying validDecimalNumber),
      "doYouPayToLookAfterYourChildren" -> nonEmptyText.verifying(validYesNo),
      "isItTheSameExpenseWhileAtWorkForChildren" -> optional(nonEmptyText.verifying(validYesNo)),
      "didYouPayToLookAfterThePersonYouCaredFor" -> nonEmptyText.verifying(validYesNo),
      "isItTheSameExpenseDuringWorkForThePersonYouCaredFor" -> optional(nonEmptyText.verifying(validYesNo))
    )(SelfEmploymentPensionsAndExpenses.apply)(SelfEmploymentPensionsAndExpenses.unapply)
      .verifying("error.required", validatePayToPension _)
  )


  def validatePayToPension(selfEmploymentPensionsAndExpenses: SelfEmploymentPensionsAndExpenses) = {
    selfEmploymentPensionsAndExpenses.doYouPayToPensionScheme match {
      case `yes` => selfEmploymentPensionsAndExpenses.howMuchDidYouPay.isDefined
      case `no` => true
    }
  }


  def present = claiming {
    implicit claim => implicit request =>
      Ok(views.html.s9_self_employment.g4_selfEmploymentPensionsAndExpenses(form.fill(SelfEmploymentPensionsAndExpenses), completedQuestionGroups))
  }

  def submit = claiming {
    implicit claim =>
      implicit request =>
        form.bindEncrypted.fold(
          formWithErrors => BadRequest(views.html.s9_self_employment.g4_selfEmploymentPensionsAndExpenses(formWithErrors, completedQuestionGroups)),
          f => claim.update(f) -> Redirect(routes.G4SelfEmploymentPensionsAndExpenses.present())
        )
  }
}
