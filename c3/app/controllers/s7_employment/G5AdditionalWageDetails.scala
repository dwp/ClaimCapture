package controllers.s7_employment

import scala.language.reflectiveCalls
import models.view.CachedClaim
import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import models.domain.AdditionalWageDetails
import utils.helpers.CarersForm._
import controllers.Mappings._
import Employment._
import play.api.data.validation.{Valid, Constraint}

object G5AdditionalWageDetails extends Controller with CachedClaim {
  val form = Form(
    mapping(
      "jobID" -> nonEmptyText,
      "oftenGetPaid" -> optional(paymentFrequency),
      "whenGetPaid" -> optional(text),
      "holidaySickPay" -> optional(text verifying validYesNo),
      "anyOtherMoney" -> (nonEmptyText verifying validYesNo),
      "otherMoney" -> optional(text verifying Constraint[String]("constraint.required") { s => Valid }),
      "employeeOwesYouMoney" -> (nonEmptyText verifying validYesNo)
    )(AdditionalWageDetails.apply)(AdditionalWageDetails.unapply)
    .verifying("otherMoney", AdditionalWageDetails.validateOtherMoney _))

  def present(jobID: String) = claiming { implicit claim => implicit request =>
    whenSectionVisible(Ok(views.html.s7_employment.g5_additionalWageDetails(form.fillWithJobID(AdditionalWageDetails, jobID), completedQuestionGroups(AdditionalWageDetails, jobID))))
  }

  def submit = claimingInJob { jobID => implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => whenSectionVisible(BadRequest(views.html.s7_employment.g5_additionalWageDetails(formWithErrors, completedQuestionGroups(AdditionalWageDetails, jobID)))),
      wageDetails => claim.update(jobs.update(wageDetails)) -> Redirect(routes.G6MoneyOwedbyEmployer.present(jobID)))
  }
}