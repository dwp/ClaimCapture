package controllers.s7_employment

import scala.language.reflectiveCalls
import models.view.CachedClaim
import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import models.domain.{AboutExpenses, CareProvider}
import utils.helpers.CarersForm._
import Employment._
import controllers.Mappings._

object G13CareProvider extends Controller with CachedClaim {
  val form = Form(
    mapping(
      "jobID" -> nonEmptyText,
      "address" -> optional(address),
      "postcode" -> optional(text)
    )(CareProvider.apply)(CareProvider.unapply))

  def present(jobID: String) = claiming { implicit claim => implicit request =>
    jobs.questionGroup(jobID, AboutExpenses) match {
      case Some(qg) if qg.asInstanceOf[AboutExpenses].payAnyoneToLookAfterPerson == `yes`=> Ok(views.html.s7_employment.g13_careProvider(form.fillWithJobID(CareProvider, jobID), completedQuestionGroups(CareProvider, jobID)))
      case _ => claim.update(jobs.delete(jobID, CareProvider)) -> Redirect(routes.G1BeenEmployed.present())
    }
  }

  def submit = claimingInJob { jobID => implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s7_employment.g13_careProvider(formWithErrors, completedQuestionGroups(CareProvider, jobID))),
      careProvider => claim.update(jobs.update(careProvider)) -> Redirect(routes.G1BeenEmployed.present()))
  }
}