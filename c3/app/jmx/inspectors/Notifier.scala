package jmx.inspectors

import org.joda.time.DateTime
import models.domain.Claim
import jmx.JMXActors._

trait RefererFilterNotifier {
  def fireNotification[R](proceed: => R = Unit) = {
    applicationInspector ! RefererRedirect
    proceed
  }
}

trait ClaimSubmissionNotifier {
  def fireNotification[R](claim: Claim)(proceed: => R = Unit) = {
    val result = proceed
    claimInspector ! ClaimSubmitted(new DateTime(claim.created), DateTime.now())
    result
  }
}

trait FastClaimsNotifier {
  def fireNotification() = claimInspector ! FastClaimDetected
}

trait ChangeOfCircsSubmissionNotifier {
  def fireNotification[R](claim: Claim)(proceed: => R = Unit) = {
    val result = proceed
    changeOfCircsInspector ! ChangeOfCircsSubmitted(new DateTime(claim.created), DateTime.now())
    result
  }
}

trait FastChangeOfCircsNotifier {
  def fireNotification() = changeOfCircsInspector ! FastChangeOfCircsDetected
}