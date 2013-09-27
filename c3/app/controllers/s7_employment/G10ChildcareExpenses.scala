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

object G10ChildcareExpenses extends Controller with CachedClaim with Navigable {
  def form(implicit claim: Claim) = Form(mapping(
    "jobID" -> nonEmptyText,
    "whoLooksAfterChildren" -> nonEmptyText,
    "howMuchCostChildcare" -> nonEmptyText.verifying(validDecimalNumber),
    "howOftenPayChildCare" -> (pensionPaymentFrequency verifying validPensionPaymentFrequencyOnly),
    "relationToYou" -> nonEmptyText,
    "relationToPartner" -> optional(nonEmptyText),
    "relationToPersonYouCare" -> nonEmptyText
  )(ChildcareExpenses.apply)(ChildcareExpenses.unapply)
    .verifying("relationToPartner.required", validateRelationToPartner(claim, _)))


  def validateRelationToPartner(implicit claim: Claim, childcareExpenses: ChildcareExpenses) = {
    claim.questionGroup(MoreAboutYou) -> claim.questionGroup(PersonYouCareFor) match {
      case (Some(m: MoreAboutYou), Some(p: PersonYouCareFor)) if m.hadPartnerSinceClaimDate == "yes" && p.isPartnerPersonYouCareFor == "no" => childcareExpenses.relationToPartner.isDefined
      case _ => true
    }
  }

  def present(jobID: String) = claiming { implicit claim => implicit request =>
    jobs.questionGroup(jobID, AboutExpenses) match {
      case Some(a: AboutExpenses) if a.payAnyoneToLookAfterChildren == `yes`=>
        track(ChildcareExpenses) { implicit claim => Ok(views.html.s7_employment.g10_childcareExpenses(form.fillWithJobID(ChildcareExpenses, jobID))) }
      case _ =>
        claim.update(jobs.delete(jobID, ChildcareExpenses)) -> Redirect(routes.G12PersonYouCareForExpenses.present(jobID))
    }
  }

  def submit = claimingInJob { jobID => implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
          .replaceError("howMuchCostChildcare", "error.required", FormError("howMuchCostChildcare", "error.required", Seq(pastPresentLabelForEmployment(claim, didYou.toLowerCase, doYou.toLowerCase , jobID))))
          .replaceError("howMuchCostChildcare", "decimal.invalid", FormError("howMuchCostChildcare", "decimal.invalid", Seq(pastPresentLabelForEmployment(claim, didYou.toLowerCase, doYou.toLowerCase , jobID))))
          .replaceError("howOftenPayChildCare", "error.required", FormError("howOftenPayChildCare", "error.required", Seq(pastPresentLabelForEmployment(claim, didYou.toLowerCase, doYou.toLowerCase , jobID))))
          .replaceError("howOftenPayChildCare.frequency","error.required", FormError("howOftenPayChildCare", "error.required", Seq(pastPresentLabelForEmployment(claim, didYou.toLowerCase, doYou.toLowerCase , jobID))))
          .replaceError("", "relationToPartner.required", FormError("relationToPartner", "error.required"))
        BadRequest(views.html.s7_employment.g10_childcareExpenses(formWithErrorsUpdate))
      },
      childcareExpenses => claim.update(jobs.update(childcareExpenses)) -> Redirect(routes.G12PersonYouCareForExpenses.present(jobID)))
  }
}