package controllers.s7_employment

import language.implicitConversions
import language.reflectiveCalls
import models.view.CachedClaim
import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import models.domain.EmployerContactDetails
import utils.helpers.CarersForm._
import controllers.Mappings._
import Employment._

object G3EmployerContactDetails extends Controller with CachedClaim {
  val form = Form(
    mapping(
      "jobID" -> nonEmptyText,
      "address" -> optional(address),
      "postcode" -> optional(text verifying validPostcode),
      "phonenumber" -> optional(text),
      call(routes.G3EmployerContactDetails.present())
    )(EmployerContactDetails.apply)(EmployerContactDetails.unapply))

  def present = claiming { implicit claim => implicit request =>
    Ok(views.html.s7_employment.g3_employerContactDetails(form, completedQuestionGroups(EmployerContactDetails)))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s7_employment.g3_employerContactDetails(formWithErrors, completedQuestionGroups(EmployerContactDetails))),
      employerContactDetails => claim.update(jobs.update(employerContactDetails)) -> Redirect(routes.G4LastWage.present()).inJob(employerContactDetails))
  }
}