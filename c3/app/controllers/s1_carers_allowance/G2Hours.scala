package controllers.s1_carers_allowance

import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import models.view.CachedClaim
import utils.helpers.CarersForm._
import models.domain.Hours
import CarersAllowance._

object G2Hours extends Controller with CachedClaim {
  val form = Form(
    mapping(
      "answer" -> boolean
    )(Hours.apply)(Hours.unapply))

  def present = claiming { implicit claim => implicit request =>
    if (CarersAllowance.claiming(Hours, claim)) Ok(views.html.s1_carers_allowance.g2_hours(confirmed = true, completedQuestionGroups(Hours)))
    else Ok(views.html.s1_carers_allowance.g2_hours(confirmed = false, completedQuestionGroups(Hours)))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => Redirect(routes.G2Hours.present()),
      hours => claim.update(hours) -> Redirect(routes.G3Over16.present()))
  }
}