package controllers.s7_employment

import scala.language.reflectiveCalls
import models.view.CachedClaim
import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import models.domain.{PersonYouCareForExpenses, ChildcareProvider}
import utils.helpers.CarersForm._
import Employment._
import controllers.Mappings

object G12PersonYouCareForExpenses extends Controller with CachedClaim {
  val form = Form(
    mapping(
      "jobID" -> nonEmptyText,
      "howMuchCostCare" -> optional(text),
      "whoDoYouPay" -> nonEmptyText,
      "relationToYou" -> optional(text)
    )(PersonYouCareForExpenses.apply)(PersonYouCareForExpenses.unapply))

  def present(jobID: String) = claiming { implicit claim => implicit request =>
    Ok(views.html.s7_employment.g12_personYouCareForExpenses(form.fillWithJobID(PersonYouCareForExpenses, jobID), completedQuestionGroups(PersonYouCareForExpenses, jobID)))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s7_employment.g12_personYouCareForExpenses(formWithErrors, completedQuestionGroups(PersonYouCareForExpenses, formWithErrors("jobID").value.get))),
      childcareProvider => claim.update(jobs.update(childcareProvider)) -> Redirect(routes.G13CareProvider.present(childcareProvider.jobID)))
  }
}