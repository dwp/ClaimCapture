package controllers.s7_employment

import scala.language.reflectiveCalls
import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.FormError
import models.view.{Navigable, CachedClaim}
import models.domain._
import utils.helpers.CarersForm._
import controllers.Mappings._
import controllers.s7_employment.Employment._
import utils.helpers.PastPresentLabelHelper._
import controllers.CarersForms._

object G10ChildcareExpenses extends Controller with CachedClaim with Navigable {
  def form(implicit claim: Claim) = Form(mapping(
    "jobID" -> nonEmptyText,
    "whoLooksAfterChildren" -> carersNonEmptyText(maxLength = sixty),
    "howMuchCostChildcare" -> nonEmptyText.verifying(validDecimalNumber),
    "howOftenPayChildCare" -> (pensionPaymentFrequency verifying validPensionPaymentFrequencyOnly),
    "relationToYou" -> nonEmptyText,
    "relationToPartner" -> optional(nonEmptyText),
    "relationToPersonYouCare" -> nonEmptyText
  )(ChildcareExpenses.apply)(ChildcareExpenses.unapply)
    .verifying("relationToPartner.required", validateRelationToPartner(claim, _)))


  def validateRelationToPartner(implicit claim: Claim, childcareExpenses: ChildcareExpenses) = {
    claim.questionGroup(MoreAboutYou) -> claim.questionGroup(YourPartnerPersonalDetails) match {
      case (Some(m: MoreAboutYou), Some(p: YourPartnerPersonalDetails)) if m.hadPartnerSinceClaimDate == "yes" && p.isPartnerPersonYouCareFor == "no" => childcareExpenses.relationToPartner.isDefined
      case _ => true
    }
  }

  def present(jobID: String) = claimingWithCheck { implicit claim => implicit request => implicit lang =>
    jobs.questionGroup(jobID, AboutExpenses) match {
      case Some(a: AboutExpenses) if a.payAnyoneToLookAfterChildren == `yes`=>
        track(ChildcareExpenses) { implicit claim => Ok(views.html.s7_employment.g10_childcareExpenses(form.fillWithJobID(ChildcareExpenses, jobID))) }
      case _ =>
        val updatedClaim = claim.update(jobs.delete(jobID, ChildcareExpenses))
        updatedClaim.update(jobs.delete(jobID, ChildcareExpenses)) -> Redirect(routes.G12PersonYouCareForExpenses.present(jobID))
    }
  }

  def submit = claimingWithCheckInJob { jobID => implicit claim => implicit request => implicit lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val pastPResentLabel = pastPresentLabelForEmployment(claim, didYou.toLowerCase, doYou.toLowerCase , jobID)
        val formWithErrorsUpdate = formWithErrors
          .replaceError("howMuchCostChildcare", "error.required", FormError("howMuchCostChildcare", "error.required", Seq(labelForEmployment(claim, "howMuchCostChildcare", jobID))))
          .replaceError("howMuchCostChildcare", "decimal.invalid", FormError("howMuchCostChildcare", "decimal.invalid", Seq(labelForEmployment(claim, "howMuchCostChildcare", jobID))))
          .replaceError("howOftenPayChildCare.frequency","error.required", FormError("howOftenPayChildCare", "error.required",Seq("",pastPResentLabel)))
          .replaceError("", "relationToPartner.required", FormError("relationToPartner", "error.required"))
          .replaceError("howOftenPayChildCare.frequency.other","error.maxLength",FormError("howOftenPayChildCare","error.maxLength",Seq("60",pastPResentLabel)))
          .replaceError("howOftenPayChildCare","error.paymentFrequency",FormError("howOftenPayChildCare","error.paymentFrequency",Seq("",pastPResentLabel)))
        BadRequest(views.html.s7_employment.g10_childcareExpenses(formWithErrorsUpdate))
      },
      childcareExpenses => claim.update(jobs.update(childcareExpenses)) -> Redirect(routes.G12PersonYouCareForExpenses.present(jobID)))
  }
}