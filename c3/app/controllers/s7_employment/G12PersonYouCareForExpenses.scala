package controllers.s7_employment

import scala.language.reflectiveCalls
import models.view.CachedClaim
import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import models.domain.{AboutExpenses, PersonYouCareForExpenses}
import utils.helpers.CarersForm._
import Employment._
import controllers.Mappings._

object G12PersonYouCareForExpenses extends Controller with CachedClaim {
  val form = Form(
    mapping(
      "jobID" -> nonEmptyText,
      "howMuchCostCare" -> optional(text),
      "whoDoYouPay" -> nonEmptyText,
      "relationToYou" -> optional(text)
    )(PersonYouCareForExpenses.apply)(PersonYouCareForExpenses.unapply))

  def present(jobID: String) = claiming { implicit claim => implicit request =>
    jobs.questionGroup(jobID, AboutExpenses) match {
      case Some(a: AboutExpenses) if a.payAnyoneToLookAfterPerson == `yes`=>
        whenSectionVisible(Ok(views.html.s7_employment.g12_personYouCareForExpenses(form.fillWithJobID(PersonYouCareForExpenses, jobID), completedQuestionGroups(PersonYouCareForExpenses, jobID))))
      case _ =>
        claim.update(jobs.delete(jobID, PersonYouCareForExpenses)) -> Redirect(routes.G14JobCompletion.present(jobID))
    }
  }

  def submit = claimingInJob { jobID => implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => whenSectionVisible(BadRequest(views.html.s7_employment.g12_personYouCareForExpenses(formWithErrors, completedQuestionGroups(PersonYouCareForExpenses, jobID)))),
      childcareProvider => claim.update(jobs.update(childcareProvider)) -> Redirect(routes.G13CareProvider.present(jobID)))
  }
}