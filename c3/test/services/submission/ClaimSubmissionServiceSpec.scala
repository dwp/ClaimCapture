package services.submission

import org.specs2.mutable.Specification
import models.domain.Claim
import helpers.ClaimBuilder._
import play.api.test.WithServer
import scala.concurrent._
import scala.concurrent.duration._

class ClaimSubmissionServiceSpec extends Specification {
  "Claim Submission service" should {
    "submit a correctly populated claim" in new WithServer() {
      val claim = Claim()
        .update(aboutYou.yourDetails)
        .update(aboutYou.claimDate)
        .update(aboutYou.contactDetails)
      val futureResp = ClaimSubmissionService.submitClaim(claim)
      val resp = Await.result(futureResp, Duration("10 seconds"))
      (resp.xml \\ "Claimant" \\ "OtherNames").text mustEqual s"${yourDetails.firstName} "
    }
  }
}
