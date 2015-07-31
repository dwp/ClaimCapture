package controllers.s_eligibility

import language.reflectiveCalls
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import models.view.CachedClaim
import utils.helpers.CarersForm._
import models.domain.Eligibility
import controllers.mappings.Mappings._
import models.view.Navigable

object GEligibility extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "hours.answer" -> nonEmptyText.verifying(validYesNo),
    "over16.answer" -> nonEmptyText.verifying(validYesNo),
    "livesInGB.answer" -> nonEmptyText.verifying(validYesNo)
  )(Eligibility.apply)(Eligibility.unapply))

  def present = claiming ({implicit claim =>  implicit request =>  lang =>
    track(Eligibility) {
      implicit claim => Ok(views.html.s_eligibility.g_eligibility(form.fill(Eligibility))(lang))
    }},checkCookie=true)

  def submit = claiming {implicit claim =>  implicit request =>  lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        BadRequest(views.html.s_eligibility.g_eligibility(formWithErrors)(lang))
      },
      f => claim.update(f) -> Redirect(routes.CarersAllowance.approve()))
  }
}