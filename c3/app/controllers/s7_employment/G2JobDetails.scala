package controllers.s7_employment

import models.view.CachedClaim
import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import models.domain.JobDetails
import utils.helpers.CarersForm._
import controllers.Mappings._

object G2JobDetails extends Controller with CachedClaim {
  val form = Form(
    mapping(
      "employerName"-> nonEmptyText,
      "jobStartDate" -> optional(dayMonthYear.verifying(validDate)),
      "finishedThisJob" -> nonEmptyText,
      "lastWorkDate" -> optional(dayMonthYear.verifying(validDate)),
      "p45LeavingDate" -> optional(dayMonthYear.verifying(validDate)),
      "hoursPerWeek" -> optional(text),
      "jobTitle" -> optional(text),
      "payrollEmployeeNumber" -> optional(text),
      call(routes.G2JobDetails.present())
    )(JobDetails.apply)(JobDetails.unapply))

  def present = claiming { implicit claim => implicit request =>
    Ok(views.html.s7_employment.g2_jobDetails(form))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s7_employment.g2_jobDetails(formWithErrors)),
      jobDetails => claim.update(jobDetails) -> Redirect(routes.G3EmployerContactDetails.present()))
  }
}