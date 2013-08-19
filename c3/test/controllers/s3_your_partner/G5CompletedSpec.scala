package controllers.s3_your_partner

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeRequest, WithApplication}
import models.domain._
import play.api.test.Helpers._

class G5CompletedSpec extends Specification with Tags {
  
  val personYouCareForInput = Seq("isPartnerPersonYouCareFor" -> "yes")
    
  "Person you care for - Controller" should {
    "present 'Completed'" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(models.view.CachedClaim.CLAIM_KEY -> claimKey)

      val result = controllers.s3_your_partner.YourPartner.completed(request)
      status(result) mustEqual OK
    }
    
    "redirect to the next page on clicking continue" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(models.view.CachedClaim.CLAIM_KEY -> claimKey)
        .withFormUrlEncodedBody(personYouCareForInput: _*)

      val result = controllers.s3_your_partner.YourPartner.completedSubmit(request)
      status(result) mustEqual SEE_OTHER
    }
  } section("unit", models.domain.YourPartner.id)
}