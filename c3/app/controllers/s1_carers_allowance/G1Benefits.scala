package controllers.s1_carers_allowance

import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import models.view.CachedClaim
import utils.helpers.CarersForm._
import models.domain.Benefits
import language.reflectiveCalls
import controllers.Mappings._
import CarersAllowance._

object G1Benefits extends Controller with CachedClaim {
  val form = Form(
    mapping(
      "answer" -> nonEmptyText.verifying(validYesNo)
    )(Benefits.apply)(Benefits.unapply))

  def present = newClaim { implicit claim => implicit request =>
    Ok(views.html.s1_carers_allowance.g1_benefits(form.fill(Benefits), completedQuestionGroups(Benefits)))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s1_carers_allowance.g1_benefits(formWithErrors, completedQuestionGroups(Benefits))),
      f => claim.update(f) -> Redirect(routes.G2Hours.present()))
  }
}