package submission

import org.specs2.mutable.{Tags, Specification}
import models.domain.Claim
import helpers.ClaimBuilder._
import play.api.test.WithServer
import scala.concurrent._
import scala.concurrent.duration._
import services.submission.{ClaimSubmissionService, ClaimSubmission}

class ClaimSubmissionServiceSpec extends Specification with Tags {
  "Claim Submission service" should {
    "submit a correctly populated claim" in new WithServer() {
      val claim = Claim()
        .update(aboutYou.yourDetails)
        .update(aboutYou.claimDate)
        .update(aboutYou.contactDetails)

      val claimSub = ClaimSubmission(claim, "TY6TV9G")
      val claimXml = claimSub.buildDwpClaim
      val futureResp = ClaimSubmissionService.submitClaim(claimXml)
      val resp = Await.result(futureResp, Duration("10 seconds"))
      (resp.xml \\ "Claimant" \\ "OtherNames").text mustEqual s"${yourDetails.firstName} "
    }
  } section "externalDependency"
}
