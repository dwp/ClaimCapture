package controllers.s2_about_you

import play.api.mvc._
import models.view.{Navigable, CachedClaim}
import models.domain._

object AboutYou extends Controller with CachedClaim with Navigable {
  def trips(implicit claim: Claim) = claim.questionGroup(Trips) match {
    case Some(ts: Trips) => ts
    case _ => Trips()
  }
}
