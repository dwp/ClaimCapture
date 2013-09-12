package controllers.s1_carers_allowance

import controllers.Mappings._
import language.reflectiveCalls
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import models.view.CachedClaim
import utils.helpers.CarersForm._
import models.domain.Benefits
import models.view.Navigable
import play.api.data.FormError

object G1Benefits extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "answer" -> nonEmptyText.verifying(validYesNo)
  )(Benefits.apply)(Benefits.unapply))

  def present = newForm { implicit claim => implicit request =>
    track(Benefits) { implicit claim => Ok(views.html.s1_carers_allowance.g1_benefits(form.fill(Benefits))) }
  }

  def submit = executeOnForm {implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
          .replaceError("answer", FormError("benefits.answer", "error.required"))
        BadRequest(views.html.s1_carers_allowance.g1_benefits(formWithErrorsUpdate))
      },
      f => claim.update(f) -> Redirect(routes.G2Hours.present()))
  }
}