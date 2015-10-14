package controllers.s_consent_and_declaration

import org.specs2.mutable.{Tags, Specification}
import play.api.test.FakeRequest
import play.api.test.Helpers._
import models.domain.{Declaration, Claiming}
import models.view.CachedClaim
import services.submission.MockInjector
import controllers.s_other_money.{GAboutOtherMoney, GAboutOtherMoneySpec}
import controllers.s_employment.GJobDetails
import utils.WithApplication

class GDeclarationSpec extends Specification with MockInjector with Tags {

  val gDeclaration = resolve(classOf[GDeclaration])


  val formInputAboutOtherMoney = Seq("anyPaymentsSinceClaimDate.answer" -> "no",
    "statutorySickPay.answer" -> "yes",
    "statutorySickPay.howMuch" -> "12",
    "statutorySickPay.howOften.frequency" -> "Other",
    "statutorySickPay.howOften.frequency.other" -> "Other Freq",
    "statutorySickPay.employersName" -> "Tesco",
    "statutorySickPay.employersAddress.lineOne" -> "Address line 1",
    "statutorySickPay.employersAddress.lineTwo" -> "Address line 2",
    "otherStatutoryPay.answer" -> "no"
  )

  "Declaration" should {
    "present" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      val result = gDeclaration.present(request)
      status(result) mustEqual OK
    }

    """enforce answer""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      val result = gDeclaration.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    """failed filling nameOrOrganisation""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
                                 .withFormUrlEncodedBody("someoneElse" -> "checked")

      val result = gDeclaration.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    """accept answers without someoneElse""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
                                 .withFormUrlEncodedBody("tellUsWhyFromAnyoneOnForm.informationFromPerson" -> "no",
                                                         "tellUsWhyFromAnyoneOnForm.whyPerson" -> "reason")

      val result = gDeclaration.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    """accept answers""" in new WithApplication with Claiming {
      val result1 = GAboutOtherMoney.submit(FakeRequest()
        withFormUrlEncodedBody(formInputAboutOtherMoney: _*))

      val request = FakeRequest().withSession(CachedClaim.key -> extractCacheKey(result1))
                                 .withFormUrlEncodedBody("tellUsWhyFromAnyoneOnForm.informationFromPerson" -> "no",
                                                         "tellUsWhyFromAnyoneOnForm.whyPerson" -> "reason",
                                                         "nameOrOrganisation"->"SomeOrg",
                                                          "someoneElse" -> "checked")

      val result = gDeclaration.submit(request)

      val claim = getClaimFromCache(result1)

      claim.questionGroup[Declaration] must beLike {
        case Some(f: Declaration) =>
          f.informationFromPerson.answer must equalTo("no")
          f.informationFromPerson.text must equalTo(Some("reason"))
          f.nameOrOrganisation must equalTo(Some("SomeOrg"))
          f.someoneElse.get must equalTo("checked")
      }
      redirectLocation(result) must beSome("/async-submitting")
    }

    """accept answers with both consent questions answered yes""" in new WithApplication with Claiming {
      val result1 = GAboutOtherMoney.submit(FakeRequest()
        withFormUrlEncodedBody(formInputAboutOtherMoney: _*))

      val request = FakeRequest().withSession(CachedClaim.key -> extractCacheKey(result1))
                                 .withFormUrlEncodedBody("tellUsWhyFromAnyoneOnForm.informationFromPerson" -> "yes",
                                                         "nameOrOrganisation"->"SomeOrg",
                                                          "someoneElse" -> "checked")

      val result = gDeclaration.submit(request)
      redirectLocation(result) must beSome("/async-submitting")
    }

  } section("unit", models.domain.ConsentAndDeclaration.id)
}