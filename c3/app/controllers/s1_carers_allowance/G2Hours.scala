package controllers.s1_carers_allowance

import language.reflectiveCalls
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import models.view.CachedClaim
import utils.helpers.CarersForm._
import models.domain.Hours
import controllers.mappings.Mappings._
import models.view.Navigable

object G2Hours extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "hours.answer" -> nonEmptyText.verifying(validYesNo)
  )(Hours.apply)(Hours.unapply))

  def present = claiming {implicit claim =>  implicit request =>  lang =>
    track(Hours) { implicit claim => Ok(views.html.s1_carers_allowance.g2_hours(form.fill(Hours))(lang)) }
  }

  def submit = claiming {implicit claim =>  implicit request =>  lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        BadRequest(views.html.s1_carers_allowance.g2_hours(formWithErrors)(lang))
      },
      f => claim.update(f) -> Redirect(routes.G3Over16.present()))
  }
}