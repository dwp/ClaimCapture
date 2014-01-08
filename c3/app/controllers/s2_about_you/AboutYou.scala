package controllers.s2_about_you

import play.api.mvc._
import models.view.{Navigable, CachedClaim}
import models.domain._
import controllers.Mappings._

object AboutYou extends Controller with CachedClaim with Navigable {
  def completed = claiming { implicit claim => implicit request =>
    track(models.domain.AboutYou) { implicit claim => Ok(views.html.s2_about_you.g10_completed()) }
  }

  def completedSubmit = claiming { implicit claim => implicit request =>
    val yourDetailsVisible = claim.questionGroup(YourDetails) match {
      case Some(y: YourDetails) => false /*TODO: Fix this*/
      case _ => true
    }
    Redirect(controllers.s3_your_partner.routes.G1YourPartnerPersonalDetails.present())
  }
}
