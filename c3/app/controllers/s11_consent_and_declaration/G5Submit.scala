package controllers.s11_consent_and_declaration

import play.api.mvc._
import models.view.CachedClaim
import models.view.Navigable
import controllers.submission._
import monitoring.ClaimBotChecking

abstract class G5Submit extends Controller with CachedClaim with Navigable {

  def present = claimingWithCheck { implicit claim => implicit request => implicit lang =>
    track(models.domain.Submit) { implicit claim => Ok(views.html.s11_consent_and_declaration.g5_submit()) }
  }

  def submit: Action[AnyContent]
}

class G5SyncSubmit extends G5Submit{
  override def submit: Action[AnyContent] = claimingWithCheck { implicit claim => implicit request => implicit lang =>
    Redirect(routes.G7Submitting.present())
  }
}

class G5AsyncSubmit extends G5Submit with AsyncSubmittable with ClaimBotChecking




