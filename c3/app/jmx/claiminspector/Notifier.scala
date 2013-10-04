package jmx.claiminspector

import org.joda.time.DateTime
import models.domain.Claim
import jmx.JMXActors._

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

trait ChangeOfCircsSubmissionNotifier {
  def fireNotification[R](claim: Claim)(action: => R) = {
    val result = action
    changeOfCircsInspector ! ChangeOfCircsSubmitted(new DateTime(claim.created), DateTime.now())
    result
  }
}

trait FastChangeOfCircsNotifier {
  def fireNotification() = changeOfCircsInspector ! FastChangeOfCircsDetected
}

trait RefererFilterNotifier {
  def fireNotification[R](proceed: => R) = {
    claimInspector ! RefererRedirect
    proceed
  }
}