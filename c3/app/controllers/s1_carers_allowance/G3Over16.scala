package controllers.s1_carers_allowance

import language.reflectiveCalls
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import models.view.CachedClaim
import utils.helpers.CarersForm._
import models.domain.Over16
import controllers.Mappings._
import models.view.Navigable

object G3Over16 extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "over16.answer" -> nonEmptyText.verifying(validYesNo)
  )(Over16.apply)(Over16.unapply))

  def present = claiming { implicit claim => implicit request => implicit lang =>
    track(Over16) { implicit claim => Ok(views.html.s1_carers_allowance.g3_over16(form.fill(Over16))) }
  }

  def submit = claiming { implicit claim => implicit request => implicit lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        BadRequest(views.html.s1_carers_allowance.g3_over16(formWithErrors))
      },
      f => claim.update(f) -> Redirect(routes.G4LivesInGB.present()))
  }
}