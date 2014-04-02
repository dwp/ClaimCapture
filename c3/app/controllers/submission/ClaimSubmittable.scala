package controllers.submission

import play.api.mvc.{AnyContent, Action, Result, Call}
import models.view.CachedClaim.ClaimResult
import models.domain.Claim


trait ClaimSubmittable {

  def submit: Action[AnyContent]

}
