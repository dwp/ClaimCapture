package controllers.s7_employment

import scala.language.reflectiveCalls
import models.view.CachedClaim
import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import models.domain.PensionSchemes
import utils.helpers.CarersForm._
import Employment._

object G7PensionSchemes extends Controller with CachedClaim {
  val form = Form(
    mapping(
      "jobID" -> nonEmptyText,
      "payOccupationalPensionScheme" -> nonEmptyText,
      "howMuchPension" -> optional(text),
      "howOftenPension" -> optional(text),
      "payPersonalPensionScheme" -> nonEmptyText,
      "howMuchPersonal" -> optional(text),
      "howOftenPersonal" -> optional(text)
    )(PensionSchemes.apply)(PensionSchemes.unapply))

  def present(jobID: String) = claiming { implicit claim => implicit request =>
    whenSectionVisible(Ok(views.html.s7_employment.g7_pensionSchemes(form.fillWithJobID(PensionSchemes, jobID), completedQuestionGroups(PensionSchemes, jobID))))
  }

  def submit = claimingInJob { jobID => implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => whenSectionVisible(BadRequest(views.html.s7_employment.g7_pensionSchemes(formWithErrors, completedQuestionGroups(PensionSchemes, jobID)))),
      schemes => claim.update(jobs.update(schemes)) -> Redirect(routes.G8AboutExpenses.present(jobID)))
  }
}