package controllers.s1_carers_allowance

import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import models.view.CachedClaim
import utils.helpers.CarersForm._
import models.domain.LivesInGB
import language.reflectiveCalls
import controllers.Mappings._

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
      formWithErrors => BadRequest(views.html.s1_carers_allowance.g4_livesInGB(formWithErrors, completedQuestionGroups(LivesInGB))),
      f => claim.update(f) -> Redirect(routes.CarersAllowance.approve()))
  }
}