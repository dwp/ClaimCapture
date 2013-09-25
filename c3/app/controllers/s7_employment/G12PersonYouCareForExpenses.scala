package controllers.s7_employment

import scala.language.reflectiveCalls
import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.FormError
import models.view.{Navigable, CachedClaim}
import models.domain.{AboutExpenses, PersonYouCareForExpenses}
import utils.helpers.CarersForm._
import Employment._
import controllers.Mappings._
import utils.helpers.PastPresentLabelHelper._

object G12PersonYouCareForExpenses extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "jobID" -> nonEmptyText,
    "whoDoYouPay" -> nonEmptyText,
    "howMuchCostCare" -> (nonEmptyText verifying validDecimalNumber),
    "howOftenPayCare" -> (pensionPaymentFrequency verifying validPensionPaymentFrequencyOnly),
    "relationToYou" -> nonEmptyText,
    "relationToPersonYouCare" -> nonEmptyText
  )(PersonYouCareForExpenses.apply)(PersonYouCareForExpenses.unapply))

  def present(jobID: String) = executeOnForm { implicit claim => implicit request =>
    jobs.questionGroup(jobID, AboutExpenses) match {
      case Some(a: AboutExpenses) if a.payAnyoneToLookAfterPerson == `yes`=>
        track(PersonYouCareForExpenses) { implicit claim => Ok(views.html.s7_employment.g12_personYouCareForExpenses(form.fillWithJobID(PersonYouCareForExpenses, jobID))) }
      case _ =>
        claim.update(jobs.delete(jobID, PersonYouCareForExpenses)) -> Redirect(routes.G14JobCompletion.present(jobID))
    }
  }

  def submit = claimingInJob { jobID => implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
          .replaceError("whoDoYouPay", "error.required", FormError("whoDoYouPay", "error.required", Seq(pastPresentLabelForEmployment(claim, didYou.toLowerCase.take(3), doYou.toLowerCase.take(2) , jobID))))
          .replaceError("howMuchCostCare", "error.required", FormError("howMuchCostCare", "error.required", Seq(pastPresentLabelForEmployment(claim, didYou.toLowerCase, doYou.toLowerCase , jobID))))
          .replaceError("howMuchCostCare", "decimal.invalid", FormError("howMuchCostCare", "decimal.invalid", Seq(pastPresentLabelForEmployment(claim, didYou.toLowerCase, doYou.toLowerCase , jobID))))
          .replaceError("howOftenPayCare", "error.required", FormError("howOftenPayCare", "error.required", Seq(pastPresentLabelForEmployment(claim, didYou.toLowerCase, doYou.toLowerCase , jobID))))

          BadRequest(views.html.s7_employment.g12_personYouCareForExpenses(formWithErrorsUpdate))
      },
      childcareProvider => claim.update(jobs.update(childcareProvider)) -> Redirect(routes.G14JobCompletion.present(jobID)))
  }
}