package controllers.submission

import org.joda.time.DateTime
import models.domain.Claim
import jmx.{FastClaimDetected, ClaimSubmitted}
import jmx.JMXActors.claimInspector

trait ClaimSubmissionNotifier {
  def fireNotification[R](claim: Claim)(action: => R) = {
    val result = action
    claimInspector ! ClaimSubmitted(new DateTime(claim.created), DateTime.now())
    result
  }
}

trait FastClaimsNotifier {
  def fireNotification() = claimInspector ! FastClaimDetected
}