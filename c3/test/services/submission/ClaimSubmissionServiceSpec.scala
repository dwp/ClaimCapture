package services.submission

import org.specs2.mutable.Specification
import models.domain.Claim
import helpers.ClaimBuilder._

class ClaimSubmissionServiceSpec extends Specification {
  "Claim Submission service" should {
    "submit a correctly populated claim" in {
      val claim = Claim()
        .update(aboutYou.yourDetails)
        .update(aboutYou.claimDate)
        .update(aboutYou.contactDetails)
        .update(aboutYou.timeOutsideUK)
      ClaimSubmissionService.submitClaim(claim) must beTrue
    }
  }
}
