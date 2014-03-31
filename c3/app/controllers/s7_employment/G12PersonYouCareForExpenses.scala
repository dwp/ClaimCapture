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
    "howMuchCostCare" -> (nonEmptyText verifying validCurrencyRequired),
    "howOftenPayCare" -> (pensionPaymentFrequency verifying validPensionPaymentFrequencyOnly),
    "relationToYou" -> nonEmptyText,
    "relationToPersonYouCare" -> nonEmptyText
  )(PersonYouCareForExpenses.apply)(PersonYouCareForExpenses.unapply))

  def present(jobID: String) = claimingWithCheck { implicit claim => implicit request => implicit lang =>
    jobs.questionGroup(jobID, AboutExpenses) match {
      case Some(a: AboutExpenses) if a.payAnyoneToLookAfterPerson == `yes`=>
        track(PersonYouCareForExpenses) { implicit claim => Ok(views.html.s7_employment.g12_personYouCareForExpenses(form.fillWithJobID(PersonYouCareForExpenses, jobID))) }
      case _ =>
        val updatedClaim = claim.update(jobs.delete(jobID, PersonYouCareForExpenses))
        updatedClaim.update(BeenEmployed(beenEmployed="")).update(jobs.completeJob(jobID))-> Redirect(routes.G2BeenEmployed.present())
    }
  }

  def submit = claimingWithCheckInJob { jobID => implicit claim => implicit request => implicit lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
          .replaceError("whoDoYouPay", "error.required", FormError("whoDoYouPay", "error.required", Seq(labelForEmployment(claim, lang, "whoDoYouPay", jobID))))
          .replaceError("whoDoYouPay", "error.restricted.characters", FormError("whoDoYouPay", "error.restricted.characters", Seq(labelForEmployment(claim, lang, "whoDoYouPay", jobID))))
          .replaceError("howMuchCostCare", "error.required", FormError("howMuchCostCare", "error.required", Seq(labelForEmployment(claim, lang, "howMuchCostCare", jobID))))
          .replaceError("howMuchCostCare", "decimal.invalid", FormError("howMuchCostCare", "decimal.invalid", Seq(labelForEmployment(claim, lang, "howMuchCostCare", jobID))))
          .replaceError("howOftenPayCare.frequency", "error.required", FormError("howOftenPayCare", "error.required", Seq("",labelForEmployment(claim, lang, "howOftenPayCare", jobID))))
          .replaceError("howOftenPayCare.frequency.other","error.maxLength",FormError("howOftenPayCare","error.maxLength",Seq("60",labelForEmployment(claim, lang, "howOftenPayCare", jobID))))
          .replaceError("howOftenPayCare","error.paymentFrequency",FormError("howOftenPayCare","error.paymentFrequency",Seq("",labelForEmployment(claim, lang, "howOftenPayCare", jobID))))

          BadRequest(views.html.s7_employment.g12_personYouCareForExpenses(formWithErrorsUpdate))
      },
      personYouCareExpenses => claim.update(jobs.update(personYouCareExpenses).completeJob(jobID)) -> Redirect(routes.G2BeenEmployed.present()))
  }
}