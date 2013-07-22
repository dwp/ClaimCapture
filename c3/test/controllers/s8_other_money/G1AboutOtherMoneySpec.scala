package controllers.s8_other_money

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeRequest, WithApplication}
import models.domain._
import play.api.test.Helpers._
import play.api.cache.Cache
import models.domain
import models.domain.Claim

class G1AboutOtherMoneySpec extends Specification with Tags {
  "About Other Money - Controller" should {
    val yourBenefits = "yes"
    val yourBenefitsText1 = "bar"
    val yourBenefitsText2 = "fizz"
    val formInput = Seq("yourBenefits.answer" -> yourBenefits, "yourBenefits.text1" -> yourBenefitsText1, "yourBenefits.text2" -> yourBenefitsText2)
    
    "present 'Your Course Details'" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val result = G1AboutOtherMoney.present(request)
      
      status(result) mustEqual OK
    }
        
    "add submitted data to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(formInput: _*)

      val result = G1AboutOtherMoney.submit(request)
      val claim = Cache.getAs[Claim](claimKey).get
      val section: Section = claim.section(domain.OtherMoney)

      section.questionGroup(AboutOtherMoney) must beLike {
        case Some(f: AboutOtherMoney) => {
          f.yourBenefits.answer must equalTo(yourBenefits)
          f.yourBenefits.text1 must equalTo(Some(yourBenefitsText1))
          f.yourBenefits.text2 must equalTo(Some(yourBenefitsText2))
        }
      }
    }
    
    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(formInput: _*)

      val result = G1AboutOtherMoney.submit(request)
      
      status(result) mustEqual SEE_OTHER
    }
  } section "unit"
}
