package controllers.s12_consent_and_declaration

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import models.domain.{Declaration, Claiming}
import models.view.CachedClaim
import services.submission.MockInjector
import controllers.s9_other_money.{G1AboutOtherMoney, G1AboutOtherMoneySpec}
import controllers.s7_employment.G3JobDetails

class G3DeclarationSpec extends Specification with MockInjector with Tags {

  val g3Declaration = resolve(classOf[G3Declaration])


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

      val result = g3Declaration.present(request)
      status(result) mustEqual OK
    }

    """enforce answer""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      val result = g3Declaration.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    """failed filling nameOrOrganisation""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
                                 .withFormUrlEncodedBody("confirm" -> "checked","someoneElse" -> "checked")

      val result = g3Declaration.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "failed to answer information from employer" in new WithApplication with Claiming {
      val result1 = G1AboutOtherMoney.submit(FakeRequest()
        withFormUrlEncodedBody(formInputAboutOtherMoney: _*))

      val result2 = g3Declaration.submit(FakeRequest().withSession(CachedClaim.key -> extractCacheKey(result1))
                    .withFormUrlEncodedBody("tellUsWhyEmployer.informationFromPerson" -> "no",
                                            "tellUsWhyEmployer.whyPerson" -> "reason",
                                            "confirm" -> "checked"))

      status(result2) mustEqual BAD_REQUEST
    }

    "faild to answer information from organisation" in new WithApplication with Claiming {
      val result1 = G1AboutOtherMoney.submit(FakeRequest()
        withFormUrlEncodedBody(formInputAboutOtherMoney: _*))

      val result2 = g3Declaration.submit(FakeRequest().withSession(CachedClaim.key -> extractCacheKey(result1))
                    .withFormUrlEncodedBody("gettingInformationFromAnyEmployer.informationFromEmployer" -> "no",
                                            "gettingInformationFromAnyEmployer.why" -> "reason",
                                            "confirm" -> "checked"))

      status(result2) mustEqual BAD_REQUEST
    }

    """accept answers without someoneElse""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
                                 .withFormUrlEncodedBody("tellUsWhyEmployer.informationFromPerson" -> "no",
                                                         "tellUsWhyEmployer.whyPerson" -> "reason",
                                                         "confirm" -> "checked")

      val result = g3Declaration.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    """accept answers""" in new WithApplication with Claiming {

      val result1 = G1AboutOtherMoney.submit(FakeRequest()
        withFormUrlEncodedBody(formInputAboutOtherMoney: _*))

      val request = FakeRequest().withSession(CachedClaim.key -> extractCacheKey(result1))
                                 .withFormUrlEncodedBody("gettingInformationFromAnyEmployer.informationFromEmployer" -> "no",
                                                         "gettingInformationFromAnyEmployer.why" -> "reason",
                                                         "tellUsWhyEmployer.informationFromPerson" -> "no",
                                                         "tellUsWhyEmployer.whyPerson" -> "reason",
                                                         "confirm" -> "checked","nameOrOrganisation"->"SomeOrg",
                                                          "someoneElse" -> "checked")

      val result = g3Declaration.submit(request)

      val claim = getClaimFromCache(result1)

      claim.questionGroup[Declaration] must beLike {
        case Some(f: Declaration) =>
          f.informationFromEmployer.answer must equalTo(Some("no"))
          f.informationFromEmployer.text must equalTo(Some("reason"))
          f.informationFromPerson.answer must equalTo("no")
          f.informationFromPerson.text must equalTo(Some("reason"))
          f.nameOrOrganisation must equalTo(Some("SomeOrg"))
          f.read must equalTo("checked")
          f.someoneElse.get must equalTo("checked")
      }
      redirectLocation(result) must beSome("/async-submitting")
    }

    """accept answers with both consent questions answered yes""" in new WithApplication with Claiming {
      val result1 = G1AboutOtherMoney.submit(FakeRequest()
        withFormUrlEncodedBody(formInputAboutOtherMoney: _*))

      val request = FakeRequest().withSession(CachedClaim.key -> extractCacheKey(result1))
                                 .withFormUrlEncodedBody("gettingInformationFromAnyEmployer.informationFromEmployer" -> "yes",
                                                         "tellUsWhyEmployer.informationFromPerson" -> "yes",
                                                         "confirm" -> "checked","nameOrOrganisation"->"SomeOrg",
                                                          "someoneElse" -> "checked")

      val result = g3Declaration.submit(request)
      redirectLocation(result) must beSome("/async-submitting")
    }

  } section("unit", models.domain.ConsentAndDeclaration.id)
}