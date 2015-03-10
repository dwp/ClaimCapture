package controllers.s1_carers_allowance

import language.reflectiveCalls
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import models.view.CachedClaim
import utils.helpers.CarersForm._
import models.domain.Eligibility
import controllers.mappings.Mappings._
import models.view.Navigable

object G2Eligibility extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "hours.answer" -> nonEmptyText.verifying(validYesNo),
    "over16.answer" -> nonEmptyText.verifying(validYesNo),
    "livesInGB.answer" -> nonEmptyText.verifying(validYesNo)
  )(Eligibility.apply)(Eligibility.unapply))

  def present = claimingWithCookie {implicit claim =>  implicit request =>  lang =>
    track(Eligibility) { implicit claim => Ok(views.html.s1_carers_allowance.g2_eligibility(form.fill(Eligibility))(lang)) }
  }

  def submit = claiming {implicit claim =>  implicit request =>  lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        BadRequest(views.html.s1_carers_allowance.g2_eligibility(formWithErrors)(lang))
      },
      f => claim.update(f) -> Redirect(routes.CarersAllowance.approve()))
  }
}