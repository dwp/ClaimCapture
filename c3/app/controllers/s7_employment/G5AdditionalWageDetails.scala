package controllers.s7_employment

import scala.language.reflectiveCalls
import models.view.CachedClaim
import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import models.domain.{AdditionalWageDetails, LastWage}
import utils.helpers.CarersForm._
import controllers.Mappings._
import Employment._

object G5AdditionalWageDetails extends Controller with CachedClaim {
  val form = Form(
    mapping(
      "jobID" -> nonEmptyText,
      "oftenGetPaid" -> optional(paymentFrequency),
      "whenGetPaid" -> optional(text),
      "holidaySickPay" -> optional(text verifying validYesNo),
      "anyOtherMoney" -> (nonEmptyText verifying validYesNo),
      "otherMoney" -> optional(text),
      "employeeOwesYouMoney" -> (nonEmptyText verifying validYesNo)
    )(AdditionalWageDetails.apply)(AdditionalWageDetails.unapply)
    .verifying("oftenGetPaid", AdditionalWageDetails.validateOftenGetPaid _)
    .verifying("otherMoney", AdditionalWageDetails.validateOtherMoney _))

  def present(jobID: String) = claiming { implicit claim => implicit request =>
    Ok(views.html.s7_employment.g5_additionalWageDetails(form.fillWithJobID(AdditionalWageDetails, jobID), completedQuestionGroups(AdditionalWageDetails, jobID)))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s7_employment.g5_additionalWageDetails(formWithErrors, completedQuestionGroups(AdditionalWageDetails, formWithErrors("jobID").value.get))),
      wageDetails => claim.update(jobs.update(wageDetails)) -> Redirect(routes.G6MoneyOwedbyEmployer.present(wageDetails.jobID)))
  }
}