package jmx.inspectors

import org.joda.time.DateTime
import models.domain.{Claimable, ChangeOfCircs, FullClaim, Claim}
import jmx.JMXActors._

trait RefererFilterNotifier {
  def fireNotification[R](proceed: => R = Unit) = {
    applicationInspector ! RefererRedirect
    proceed
  }
}

trait SubmissionNotifier {
  def fireNotification[R](claim: Claim)(proceed: => R = Unit) = {
    val result = proceed
    claim match{
      case c: Claim with FullClaim => claimInspector ! ClaimSubmitted(new DateTime(claim.created), DateTime.now())
      case c: Claim with ChangeOfCircs => changeOfCircsInspector ! ChangeOfCircsSubmitted(new DateTime(claim.created), DateTime.now())
    }

    result
  }

}

trait FastSubmissionNotifier {

  def fireFastNotification[R](claim: Claim) = {
    claim match{
      case c: Claim with FullClaim => claimInspector ! FastClaimDetected
      case c: Claim with ChangeOfCircs => changeOfCircsInspector ! FastChangeOfCircsDetected
    }
  }
}