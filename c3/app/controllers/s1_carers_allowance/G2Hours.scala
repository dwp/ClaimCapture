package controllers.s1_carers_allowance

import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import models.view.CachedClaim
import utils.helpers.CarersForm._
import models.domain.Hours
import language.reflectiveCalls
import controllers.Mappings._

object G2Hours extends Controller with CarersAllowanceRouting with CachedClaim {
  val form = Form(
    mapping(
      "answer" -> nonEmptyText.verifying(validYesNo)
    )(Hours.apply)(Hours.unapply))

  def present = claiming { implicit claim => implicit request =>
    Ok(views.html.s1_carers_allowance.g2_hours(form.fill(Hours), completedQuestionGroups(Hours)))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s1_carers_allowance.g2_hours(formWithErrors, completedQuestionGroups(Hours))),
      f => claim.update(f) -> Redirect(routes.G3Over16.present()))
  }
}