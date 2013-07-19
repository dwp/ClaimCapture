package controllers.s8_other_money

import org.specs2.mutable.Specification
import org.specs2.mutable.Tags

import models.NationalInsuranceNumber
import models.domain
import models.domain.Claim
import models.domain.Claiming
import models.domain.PersonWhoGetsThisMoney
import models.domain.Section
import play.api.cache.Cache
import play.api.test.FakeRequest
import play.api.test.Helpers.BAD_REQUEST
import play.api.test.Helpers.OK
import play.api.test.Helpers.SEE_OTHER
import play.api.test.Helpers.status
import play.api.test.WithApplication

class G3PersonWhoGetsThisMoneySpec extends Specification with Tags {

  "Person Who Gets The Money - Controller" should {
    val fullName = "Donald Duck"
    val ni1 = "AB"
    val ni2 = 12
    val ni3 = 34
    val ni4 = 56
    val ni5 = "C"
    val nameOfBenefit = "foo"

    val formInput = Seq("fullName" -> fullName,
      "nationalInsuranceNumber.ni1" -> ni1,
      "nationalInsuranceNumber.ni2" -> ni2.toString,
      "nationalInsuranceNumber.ni3" -> ni3.toString,
      "nationalInsuranceNumber.ni4" -> ni4.toString,
      "nationalInsuranceNumber.ni5" -> ni5,
      "nameOfBenefit" -> nameOfBenefit)

    "present 'Person Who Gets The Money' " in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val result = G3PersonWhoGetsThisMoney.present(request)

      status(result) mustEqual OK
    }

    "return a bad request after an invalid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody("foo" -> "bar")

      val result = G3PersonWhoGetsThisMoney.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "add submitted form to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(formInput: _*)

      val result = G3PersonWhoGetsThisMoney.submit(request)
      val claim = Cache.getAs[Claim](claimKey).get
      val section: Section = claim.section(domain.OtherMoney)

      section.questionGroup(PersonWhoGetsThisMoney) must beLike {
        case Some(f: PersonWhoGetsThisMoney) => {
          f.fullName must equalTo(fullName)
          f.nationalInsuranceNumber must equalTo(Some(NationalInsuranceNumber(Some(ni1), Some(ni2.toString), Some(ni3.toString), Some(ni4.toString), Some(ni5))))
          f.nameOfBenefit must equalTo(nameOfBenefit)
        }
      }
    }

    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(formInput: _*)

      val result = G3PersonWhoGetsThisMoney.submit(request)
      status(result) mustEqual SEE_OTHER
    }
  } section "unit"
}