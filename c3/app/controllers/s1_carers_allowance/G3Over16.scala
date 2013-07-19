package controllers.s1_carers_allowance

import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import models.view.CachedClaim
import utils.helpers.CarersForm._
import models.domain.{Over16, Claim}

object G3Over16 extends Controller with CachedClaim {
  val form = Form(
    mapping(
      "call" -> ignored(routes.G3Over16.present()),
      "answer" -> boolean
    )(Over16.apply)(Over16.unapply))

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(Over16)

  def present = claiming { implicit claim => implicit request =>
    if (CarersAllowance.claiming(Over16, claim)) Ok(views.html.s1_carers_allowance.g3_over16(confirmed = true, completedQuestionGroups))
    else Ok(views.html.s1_carers_allowance.g3_over16(confirmed = false, completedQuestionGroups))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => Redirect(routes.G3Over16.present()),
      over16 => claim.update(over16) -> Redirect(routes.G4LivesInGB.present()))
  }
}