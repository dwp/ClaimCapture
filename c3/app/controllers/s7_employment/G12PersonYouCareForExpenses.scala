package controllers.s7_employment

import scala.language.reflectiveCalls
import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import models.view.{Navigable, CachedClaim}
import models.domain.{BeenEmployed, AboutExpenses, PersonYouCareForExpenses}
import utils.helpers.CarersForm._
import Employment._
import controllers.Mappings._
import utils.helpers.PastPresentLabelHelper._
import controllers.CarersForms._
import play.api.data.FormError
import scala.Some

object G12PersonYouCareForExpenses extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "jobID" -> nonEmptyText,
    "whoDoYouPay" -> carersNonEmptyText,
    "howMuchCostCare" -> (nonEmptyText verifying validDecimalNumber),
    "howOftenPayCare" -> (pensionPaymentFrequency verifying validPensionPaymentFrequencyOnly),
    "relationToYou" -> nonEmptyText,
    "relationToPersonYouCare" -> nonEmptyText
  )(PersonYouCareForExpenses.apply)(PersonYouCareForExpenses.unapply))

  def present(jobID: String) = claiming { implicit claim => implicit request =>
    jobs.questionGroup(jobID, AboutExpenses) match {
      case Some(a: AboutExpenses) if a.payAnyoneToLookAfterPerson == `yes`=>
        track(PersonYouCareForExpenses) { implicit claim => Ok(views.html.s7_employment.g12_personYouCareForExpenses(form.fillWithJobID(PersonYouCareForExpenses, jobID))) }
      case _ =>
        claim.update(jobs.delete(jobID, PersonYouCareForExpenses))
        claim.update(BeenEmployed(beenEmployed="")).update(jobs.completeJob(jobID))-> Redirect(routes.G1BeenEmployed.present())
    }
  }

  def submit = claimingInJob { jobID => implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val pastPresentLabel = pastPresentLabelForEmployment(claim, didYou.toLowerCase, doYou.toLowerCase, jobID)
        val formWithErrorsUpdate = formWithErrors
          .replaceError("whoDoYouPay", "error.required", FormError("whoDoYouPay", "error.required", Seq(pastPresentLabelForEmployment(claim, didYou.toLowerCase.take(3), doYou.toLowerCase.take(2) , jobID))))
          .replaceError("whoDoYouPay", "error.restricted.characters", FormError("whoDoYouPay", "error.restricted.characters", Seq(pastPresentLabelForEmployment(claim, didYou.toLowerCase.take(3), doYou.toLowerCase.take(2) , jobID))))
          .replaceError("howMuchCostCare", "error.required", FormError("howMuchCostCare", "error.required", Seq(pastPresentLabel)))
          .replaceError("howMuchCostCare", "decimal.invalid", FormError("howMuchCostCare", "decimal.invalid", Seq(pastPresentLabel)))
          .replaceError("howOftenPayCare.frequency", "error.required", FormError("howOftenPayCare", "error.required", Seq("",pastPresentLabel)))
          .replaceError("howOftenPayCare.frequency.other","error.maxLength",FormError("howOftenPayCare","error.maxLength",Seq("60",pastPresentLabel)))
          .replaceError("howOftenPayCare","error.paymentFrequency",FormError("howOftenPayCare","error.paymentFrequency",Seq("",pastPresentLabel)))

          BadRequest(views.html.s7_employment.g12_personYouCareForExpenses(formWithErrorsUpdate))
      },
      childcareProvider => claim.update(jobs.update(childcareProvider)) -> Redirect(routes.G1BeenEmployed.present()))
  }
}