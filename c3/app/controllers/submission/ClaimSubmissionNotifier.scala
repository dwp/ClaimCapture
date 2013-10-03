package controllers.submission

import org.joda.time.DateTime
import models.domain.Claim
import jmx.ClaimSubmitted
import jmx.JMXActors._

trait ClaimSubmissionNotifier {
  def notity[R](claim: Claim)(f: => R) = {
    claimInspector ! ClaimSubmitted(new DateTime(claim.created), DateTime.now())
    f
  }
}