package controllers.s1_carers_allowance

import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import models.view.CachedClaim
import utils.helpers.CarersForm._
import models.domain.Hours
import language.reflectiveCalls
import controllers.Mappings._
import play.api.data.FormError

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
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
          .replaceError("answer", FormError("hours.answer", "error.required"))
        BadRequest(views.html.s1_carers_allowance.g2_hours(formWithErrorsUpdate, completedQuestionGroups(Hours)))
      },
      f => claim.update(f) -> Redirect(routes.G3Over16.present()))
  }
}