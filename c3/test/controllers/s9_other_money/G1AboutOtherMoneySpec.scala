package controllers.s9_other_money

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeRequest, WithApplication}
import models.domain._
import play.api.test.Helpers._
import play.api.cache.Cache
import models.domain
import models.domain.Claim
import models.view.CachedClaim

class G1AboutOtherMoneySpec extends Specification with Tags {
  "Details about other money - Controller" should {
    val yourBenefits = "yes"
    val anyPaymentsSinceClaimDate = "yes"
    val formInput = Seq("yourBenefits.answer" -> yourBenefits, 
        "anyPaymentsSinceClaimDate.answer" -> anyPaymentsSinceClaimDate)
    
    "present 'Your course details'" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.claimKey -> claimKey)

      val result = G1AboutOtherMoney.present(request)
      
      status(result) mustEqual OK
    }
        
    "add submitted data to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.claimKey -> claimKey)
        .withFormUrlEncodedBody(formInput: _*)

      val result = G1AboutOtherMoney.submit(request)
      val claim = Cache.getAs[Claim](claimKey).get
      val section: Section = claim.section(domain.OtherMoney)

      section.questionGroup(AboutOtherMoney) must beLike {
        case Some(f: AboutOtherMoney) => {
          f.yourBenefits.answer must equalTo(yourBenefits)
          f.anyPaymentsSinceClaimDate.answer must equalTo(anyPaymentsSinceClaimDate)
        }
      }
    }
    
    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.claimKey -> claimKey)
        .withFormUrlEncodedBody(formInput: _*)

      val result = G1AboutOtherMoney.submit(request)
      
      status(result) mustEqual SEE_OTHER
    }
  } section("unit", models.domain.OtherMoney.id)
}
