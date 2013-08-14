package controllers.s1_carers_allowance

import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import models.view.CachedClaim
import utils.helpers.CarersForm._
import models.domain.LivesInGB
import language.reflectiveCalls
import controllers.Mappings._
import play.api.data.FormError

object G4LivesInGB extends Controller with CarersAllowanceRouting with CachedClaim {
  val form = Form(
    mapping(
      "answer" -> nonEmptyText.verifying(validYesNo)
    )(LivesInGB.apply)(LivesInGB.unapply))

  def present = claiming { implicit claim => implicit request =>
    Ok(views.html.s1_carers_allowance.g4_livesInGB(form.fill(LivesInGB), completedQuestionGroups(LivesInGB)))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
          .replaceError("answer", FormError("livesInGB.answer", "error.required"))
        BadRequest(views.html.s1_carers_allowance.g4_livesInGB(formWithErrorsUpdate, completedQuestionGroups(LivesInGB)))
      },
      f => claim.update(f) -> Redirect(routes.CarersAllowance.approve()))
  }
}