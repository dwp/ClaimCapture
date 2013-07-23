package controllers.s7_employment

import models.view.CachedClaim
import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import models.domain.PensionSchemes
import utils.helpers.CarersForm._
import controllers.Mappings._
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
      "howOftenPersonal" -> optional(text),
      call(routes.G6MoneyOwedbyEmployer.present())
    )(PensionSchemes.apply)(PensionSchemes.unapply))


  def present = claiming { implicit claim => implicit request =>
    Ok(views.html.s7_employment.g7_pensionSchemes(form, completedQuestionGroups(PensionSchemes)))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s7_employment.g7_pensionSchemes(formWithErrors, completedQuestionGroups(PensionSchemes))),
      schemes => claim.update(schemes) -> Redirect(routes.G7PensionSchemes.present()))
  }
}