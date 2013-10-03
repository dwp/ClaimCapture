package controllers.submission

import org.joda.time.DateTime
import models.domain.Claim
import jmx.ClaimSubmitted


trait ClaimSubmissionNotifier {
  def fireNotification[R](claim: Claim)(action: => R) = {
    val result = action

    jmx.JMXActors.claimInspector ! ClaimSubmitted(new DateTime(claim.created), DateTime.now())

    result
  }
}