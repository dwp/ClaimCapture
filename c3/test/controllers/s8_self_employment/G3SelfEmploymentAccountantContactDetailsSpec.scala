package controllers.s8_self_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeRequest, WithApplication}
import models.domain._
import play.api.test.Helpers._
import play.api.cache.Cache
import models.MultiLineAddress
import scala.Some


class G3SelfEmploymentAccountantContactDetailsSpec extends Specification with Tags {

  "Self Employment - Accountant Contact Details - Controller" should {
    val accountantsName = "Hello123"
    val lineOne = "lineOne"
    val lineTwo = "lineTwo"
    val lineThree = "lineThree"
    val postCode = "EC1A 4JQ"
    val telephoneNumber = "1234456"
    val faxNumber = "12334456"


    val accountantContactDetailsInput = Seq("accountantsName" -> accountantsName,
      "address.lineOne" -> lineOne.toString,
      "address.lineTwo" -> lineTwo.toString,
      "address.lineThree" -> lineThree.toString,
      "postcode" -> postCode.toString,
      "telephoneNumber" -> telephoneNumber,
      "faxNumber" -> faxNumber
    )

    "present 'Accountant Contact Details' " in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody("areAccountsPreparedOnCashFlowBasis" -> "yes", "doYouHaveAnAccountant" -> "yes", "canWeContactYourAccountant" -> "no")

      val result = G2SelfEmploymentYourAccounts.submit(request)

      val request2 = FakeRequest().withSession("connected" -> claimKey)

      val result2 = G3SelfEmploymentAccountantContactDetails.present(request)
      status(result2) mustEqual OK
    }

    "add submitted form to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(accountantContactDetailsInput: _*)

      val result = controllers.s8_self_employment.G3SelfEmploymentAccountantContactDetails.submit(request)
      val claim = Cache.getAs[Claim](claimKey).get
      val section: Section = claim.section(models.domain.SelfEmployment)
      section.questionGroup(SelfEmploymentAccountantContactDetails) must beLike {
        case Some(f: SelfEmploymentAccountantContactDetails) => {
          f.accountantsName must equalTo(accountantsName)
          f.address must equalTo(MultiLineAddress(Some(lineOne), Some(lineTwo), Some(lineThree)))
          f.postcode must equalTo(Some(postCode))
          f.telephoneNumber must equalTo(Some(telephoneNumber))
          f.faxNumber must equalTo(Some(faxNumber))
        }
      }
    }

    "missing mandatory field" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody("accountantsName" -> "")

      val result = controllers.s8_self_employment.G3SelfEmploymentAccountantContactDetails.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(accountantContactDetailsInput: _*)

      val result = controllers.s8_self_employment.G3SelfEmploymentAccountantContactDetails.submit(request)
      status(result) mustEqual SEE_OTHER
    }

  } section("unit",models.domain.SelfEmployment.id)

}
