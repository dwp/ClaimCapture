package controllers.s7_employment

import scala.language.reflectiveCalls
import models.view.CachedClaim
import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import models.domain.{AboutExpenses, ChildcareProvider}
import utils.helpers.CarersForm._
import controllers.Mappings._
import Employment._

object G11ChildcareProvider extends Controller with CachedClaim {
  val form = Form(
    mapping(
      "jobID" -> nonEmptyText,
      "address" -> optional(address),
      "postcode" -> optional(text)
    )(ChildcareProvider.apply)(ChildcareProvider.unapply))

  def present(jobID: String) = claiming { implicit claim => implicit request =>
    jobs.questionGroup(jobID, AboutExpenses) match {
      case Some(qg) if qg.asInstanceOf[AboutExpenses].payAnyoneToLookAfterChildren == `yes`=> Ok(views.html.s7_employment.g11_childcareProvider(form.fillWithJobID(ChildcareProvider, jobID), completedQuestionGroups(ChildcareProvider, jobID)))
      case _ => claim.update(jobs.delete(jobID, ChildcareProvider)) -> Redirect(routes.G12PersonYouCareForExpenses.present(jobID))
    }
  }

  def submit = claimingInJob { jobID => implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s7_employment.g11_childcareProvider(formWithErrors, completedQuestionGroups(ChildcareProvider, jobID))),
      childcareProvider => claim.update(jobs.update(childcareProvider)) -> Redirect(routes.G12PersonYouCareForExpenses.present(jobID)))
  }
}