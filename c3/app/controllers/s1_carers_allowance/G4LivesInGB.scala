package controllers.s1_carers_allowance

import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import models.view.CachedClaim
import utils.helpers.CarersForm._
import models.domain.LivesInGB
import CarersAllowance._

object G4LivesInGB extends Controller with CachedClaim {
  val form = Form(
    mapping(
      "answer" -> boolean
    )(LivesInGB.apply)(LivesInGB.unapply))

  def present = claiming { implicit claim => implicit request =>
    if (CarersAllowance.claiming(LivesInGB, claim)) Ok(views.html.s1_carers_allowance.g4_livesInGB(confirmed = true, completedQuestionGroups(LivesInGB)))
    else Ok(views.html.s1_carers_allowance.g4_livesInGB(confirmed = false, completedQuestionGroups(LivesInGB)))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => Redirect(routes.G4LivesInGB.present()),
      livesInGB => claim.update(livesInGB) -> Redirect(routes.CarersAllowance.approve()))
  }
}