package controllers.s7_employment

import scala.language.reflectiveCalls
import models.view.CachedClaim
import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import models.domain.{ChildcareProvider, ChildcareExpenses}
import utils.helpers.CarersForm._
import Employment._
import controllers.Mappings

object G11ChildcareProvider extends Controller with CachedClaim {
  val form = Form(
    mapping(
      "jobID" -> nonEmptyText,
      "address" -> optional(Mappings.address),
      "postcode" -> optional(text)
    )(ChildcareProvider.apply)(ChildcareProvider.unapply))

  def present(jobID: String) = claiming { implicit claim => implicit request =>
    Ok(views.html.s7_employment.g11_childcareProvider(form.fillWithJobID(ChildcareProvider, jobID), completedQuestionGroups(ChildcareProvider, jobID)))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s7_employment.g11_childcareProvider(formWithErrors, completedQuestionGroups(ChildcareProvider, formWithErrors("jobID").value.get))),
      childcareProvider => claim.update(jobs.update(childcareProvider)) -> Redirect(routes.G12PersonYouCareForExpenses.present(childcareProvider.jobID)))
  }
}