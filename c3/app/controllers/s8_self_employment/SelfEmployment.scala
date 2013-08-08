package controllers.s8_self_employment

import play.api.mvc._
import play.api.templates.Html
import models.view.CachedClaim
import models.domain._
import controllers.Routing
import play.api.mvc.SimpleResult

object SelfEmployment extends Controller with SelfEmploymentRouting with CachedClaim {
  def whenSectionVisible(f: => SimpleResult[Html])(implicit claim: Claim) = {
    if (claim.isSectionVisible(models.domain.SelfEmployment)) f
    else Redirect(controllers.s9_other_money.routes.G1AboutOtherMoney.present())
  }

  def completed = claiming { implicit claim => implicit request =>
    whenSectionVisible(Ok(views.html.s8_self_employment.g9_completed(completedQuestionGroups.map(qg => qg -> route(qg)))))
  }

  def completedSubmit = claiming { implicit claim => implicit request =>
    Redirect(claim.nextSection(models.domain.SelfEmployment).firstPage)
  }

  private def completedQuestionGroups(implicit claim: Claim): List[QuestionGroup] = {
    claim.completedQuestionGroups(models.domain.SelfEmployment)
  }
}

trait SelfEmploymentRouting extends Routing {
  override def route(qgi: QuestionGroup.Identifier) = qgi match {
    case AboutSelfEmployment => routes.G1AboutSelfEmployment.present()
    case SelfEmploymentYourAccounts => routes.G2SelfEmploymentYourAccounts.present()
    case SelfEmploymentAccountantContactDetails => routes.G3SelfEmploymentAccountantContactDetails.present()
    case SelfEmploymentPensionsAndExpenses => routes.G4SelfEmploymentPensionsAndExpenses.present()
    case ChildcareExpensesWhileAtWork => routes.G5ChildcareExpensesWhileAtWork.present()
    case ChildcareProvidersContactDetails => routes.G6ChildcareProvidersContactDetails.present()
    case ExpensesWhileAtWork => routes.G7ExpensesWhileAtWork.present()
    case CareProvidersContactDetails => routes.G8CareProvidersContactDetails.present()
  }
}