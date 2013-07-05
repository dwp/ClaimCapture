package controllers.s1_carers_allowance

import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import models.view.CachedClaim
import controllers.Routing
import utils.helpers.CarersForm._
import models.domain.Benefits

object G1Benefits extends Controller with Routing with CachedClaim {
  override val route = Benefits.id -> routes.G1Benefits.present

  val form = Form(
    mapping(
      "answer" -> boolean
    )(Benefits.apply)(Benefits.unapply))
    
  def present = newClaim { implicit claim => implicit request =>
    if (CarersAllowance.claiming(Benefits, claim)) Ok(views.html.s1_carers_allowance.g1_benefits(confirmed = true))
    else Ok(views.html.s1_carers_allowance.g1_benefits(confirmed = false))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => Redirect(routes.G1Benefits.present()),
      benefits => claim.update(benefits) -> Redirect(routes.G2Hours.present()))
  }
}