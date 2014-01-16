package controllers.s2_about_you

import play.api.mvc._
import models.view.{Navigable, CachedClaim}
import models.domain._
import controllers.Mappings._

object AboutYou extends Controller with CachedClaim with Navigable {
  def trips(implicit claim: Claim) = claim.questionGroup(Trips) match {
    case Some(ts: Trips) => ts
    case _ => Trips()
  }

  def completed = claiming { implicit claim => implicit request => implicit lang =>
    track(models.domain.AboutYou) { implicit claim => Ok(views.html.s2_about_you.g10_completed()) }
  }

  def completedSubmit = claiming { implicit claim => implicit request => implicit lang =>
    val yourDetailsVisible = claim.questionGroup(YourDetails) match {
      case Some(y: YourDetails) => false /*TODO: Fix this*/
      case _ => true
    }
    Redirect(controllers.s3_your_partner.routes.G1YourPartnerPersonalDetails.present())
  }
}
