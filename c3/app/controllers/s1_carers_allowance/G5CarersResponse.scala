package controllers.s1_carers_allowance

import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import models.view.CachedClaim
import models.domain.LivesInGB

object G5CarersResponse extends Controller with CachedClaim {
  val form = Form(
    mapping(
      "answer" -> boolean
    )(LivesInGB.apply)(LivesInGB.unapply))

  def present = claiming { implicit claim => implicit request =>
    Ok(views.html.s1_carers_allowance.g5_carersResponse())
  }

  def submit = claiming { implicit claim => implicit request =>
    Redirect(claim.nextSection(models.domain.CarersAllowance).firstPage)
  }
}