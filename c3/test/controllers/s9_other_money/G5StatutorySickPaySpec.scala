package controllers.s9_other_money

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import play.api.cache.Cache
import models.MultiLineAddress
import scala.Some
import models.domain.{Section, StatutorySickPay, Claim, Claiming}
import models.PaymentFrequency

class G5StatutorySickPaySpec extends Specification with Tags {
  "Other Money - Statutory Pay - Controller" should {
    val haveYouHadAnyStatutorySickPay = "yes"
    val howMuch = "bar"
    val howOften_frequency = "Weekly"
    val howOften_other = "other"
    val employersName = "Johny B Good"
    val employersAddressLineOne =  "lineOne"
    val employersAddressLineTwo = "lineTwo"
    val employersAddressLineThree = "lineThree"
    val employersPostcode = "SE1 6EH"
      
    val statutorySickPayInput = Seq("haveYouHadAnyStatutorySickPay" -> haveYouHadAnyStatutorySickPay,
      "howMuch" -> howMuch,
      "howOften.frequency" -> howOften_frequency,
      "howOften.other" -> howOften_other,
      "employersName" -> employersName,
      "employersAddress.lineOne" -> employersAddressLineOne, 
      "employersAddress.lineTwo" -> employersAddressLineTwo, 
      "employersAddress.lineThree" -> employersAddressLineThree,
      "employersPostcode" -> employersPostcode)
            
    "present 'Statutory Pay' " in new WithApplication with Claiming {
      val request = FakeRequest().withSession(models.view.CachedClaim.CLAIM_KEY -> claimKey)

      val result = controllers.s9_other_money.G5StatutorySickPay.present(request)
      status(result) mustEqual OK
    }

    "add submitted form to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(models.view.CachedClaim.CLAIM_KEY -> claimKey)
        .withFormUrlEncodedBody(statutorySickPayInput: _*)

      val result = controllers.s9_other_money.G5StatutorySickPay.submit(request)
      val claim = Cache.getAs[Claim](claimKey).get
      val section: Section = claim.section(models.domain.OtherMoney)

      section.questionGroup(StatutorySickPay) must beLike {
        case Some(f: StatutorySickPay) => {
          f.haveYouHadAnyStatutorySickPay must equalTo(haveYouHadAnyStatutorySickPay)
          f.howMuch must equalTo(Some(howMuch))
          f.howOften must equalTo(Some(PaymentFrequency(howOften_frequency, Some(howOften_other))))
          f.employersName must equalTo(Some(employersName))
          f.employersAddress must equalTo(Some(MultiLineAddress(Some(employersAddressLineOne), Some(employersAddressLineTwo), Some(employersAddressLineThree))))
          f.employersPostcode must equalTo(Some(employersPostcode))
        }
      }
    }

    "return a bad request after an invalid submission" in {
      "invalid postcode" in new WithApplication with Claiming {
        val request = FakeRequest().withSession(models.view.CachedClaim.CLAIM_KEY -> claimKey)
          .withFormUrlEncodedBody("haveYouHadAnyStatutorySickPay" -> haveYouHadAnyStatutorySickPay, 
              "employersName" -> employersName,
              "employersPostcode" -> "INVALID")
  
        val result = controllers.s9_other_money.G5StatutorySickPay.submit(request)
        status(result) mustEqual BAD_REQUEST
      }
      
      "missing mandatory field" in new WithApplication with Claiming {
        val request = FakeRequest().withSession(models.view.CachedClaim.CLAIM_KEY -> claimKey)
          .withFormUrlEncodedBody("haveYouHadAnyStatutorySickPay" -> haveYouHadAnyStatutorySickPay)
  
        val result = controllers.s9_other_money.G5StatutorySickPay.submit(request)
        status(result) mustEqual BAD_REQUEST
      }
    }

    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(models.view.CachedClaim.CLAIM_KEY -> claimKey)
        .withFormUrlEncodedBody(statutorySickPayInput: _*)

      val result = controllers.s9_other_money.G5StatutorySickPay.submit(request)
      status(result) mustEqual SEE_OTHER
    }
  } section("unit", models.domain.OtherMoney.id)
}