package controllers.submission

import play.api.mvc.{Result, Call}
import models.view.CachedClaim.ClaimResult
import models.domain.Claim


trait ClaimSubmittable {

  def submissionRoute(claim:Claim): Call

  def submitAction(claim:Claim): Either[Result, ClaimResult]

}
