package controllers.s3_your_partner

import org.specs2.mutable.Specification
import play.api.test.{FakeRequest, WithApplication}
import play.api.cache.Cache
import models.domain._
import models.{DayMonthYear, domain}
import play.api.test.Helpers._
import models.domain.Claim

class G5CompletedSpec extends Specification {
  
  val personYouCareForInput = Seq("isPartnerPersonYouCareFor" -> "yes")
    
  "Person You Care For - Controller" should {
    "present 'Completed'" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val result = controllers.s3_your_partner.YourPartner.completed(request)
      status(result) mustEqual OK
    }
    
    "redirect to the next page on clicking continue" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(personYouCareForInput: _*)

      val result = controllers.s3_your_partner.YourPartner.completedSubmit(request)
      status(result) mustEqual SEE_OTHER
    }
  }
}