package controllers.s1_carers_allowance

import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import models.view.CachedClaim
import controllers.Routing
import utils.helpers.CarersForm._
import models.domain.{Over16, Claim}

object G3Over16 extends Controller with Routing with CachedClaim {
  override val route = Over16.id -> routes.G3Over16.present

  val form = Form(
    mapping(
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