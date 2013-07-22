package controllers.s7_employment

import language.implicitConversions
import language.reflectiveCalls
import models.view.CachedClaim
import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import models.domain.JobDetails
import utils.helpers.CarersForm._
import controllers.Mappings._
import Employment._

object G2JobDetails extends Controller with CachedClaim {
  val form = Form(
    mapping(
      "jobID" -> nonEmptyText,
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

  /*def break(id: String) = claiming { implicit claim => implicit request =>
    claim.questionGroup(BreaksInCare) match {
      case Some(b: BreaksInCare) => b.breaks.find(_.id == id) match {
        case Some(b: Break) => Ok(views.html.s4_care_you_provide.g11_break(form.fill(b)))
        case _ => Redirect(routes.G10BreaksInCare.present())
      }

      case _ => Redirect(routes.G10BreaksInCare.present())
    }
  }*/

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s7_employment.g2_jobDetails(formWithErrors)),
      jobDetails => claim.update(jobs.update(jobDetails)) -> Redirect(routes.G3EmployerContactDetails.present()).inJob(jobDetails))
  }
}