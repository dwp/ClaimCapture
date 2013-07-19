package controllers.s8_other_money

import org.specs2.mutable.{ Tags, Specification }
import play.api.test.{ FakeRequest, WithApplication }
import models.domain._
import play.api.test.Helpers._
import play.api.cache.Cache
import models.domain
import models.domain.Claim

class G2MoneyPaidToSomeoneElseForYouSpec extends Specification with Tags {
  "Money paid to someone else for you - Controller" should {
    val moneyAddedToBenefitSinceClaimDate = "yes"
    val formInput = Seq("moneyAddedToBenefitSinceClaimDate" -> moneyAddedToBenefitSinceClaimDate)

    "present" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val result = G2MoneyPaidToSomeoneElseForYou.present(request)

      status(result) mustEqual OK
    }
    
    "add submitted data to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(formInput: _*)

      val result = G2MoneyPaidToSomeoneElseForYou.submit(request)
      val claim = Cache.getAs[Claim](claimKey).get
      val section: Section = claim.section(domain.OtherMoney)

      section.questionGroup(MoneyPaidToSomeoneElseForYou) must beLike {
        case Some(f: MoneyPaidToSomeoneElseForYou) => {
          f.moneyAddedToBenefitSinceClaimDate must equalTo(moneyAddedToBenefitSinceClaimDate)
        }
      }
    }

    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(formInput: _*)

      val result = G2MoneyPaidToSomeoneElseForYou.submit(request)
      
      status(result) mustEqual SEE_OTHER
    }
  }
}