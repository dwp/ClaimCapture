package controllers.circs.s3_consent_and_declaration

import play.api.test.{FakeApplication, FakeRequest, WithApplication}
import models.domain._
import models.view.CachedChangeOfCircs
import play.api.cache.Cache
import play.api.test.Helpers._
import org.specs2.mutable.{Tags, Specification}
import services.submission.MockInjector

class G1DeclarationSpec extends Specification with MockInjector with Tags {

  val byPost = "By Post"
  val infoAgreement = "yes"
  val why = "Cause i want"
  val confirm = "yes"
  val someOneElse = "checked"
  val nameOrOrganisation = "Tesco"

  val declarationInput = Seq("furtherInfoContact" -> byPost, "obtainInfoAgreement" -> infoAgreement, "obtainInfoWhy" -> why, "confirm" -> confirm, "circsSomeOneElse" -> someOneElse, "nameOrOrganisation" -> nameOrOrganisation)
  val declartionInputWithoutSomeOne = Seq("furtherInfoContact" -> byPost, "obtainInfoAgreement" -> infoAgreement, "obtainInfoWhy" -> why, "confirm" -> confirm, "circsSomeOneElse" -> "")

  val G1Declaration = resolve(classOf[G1Declaration])

  "Circumstances - OtherChangeInfo - Controller" should {

    "present 'Other Change Information' " in new WithApplication(app = FakeApplication(withGlobal = Some(global))) with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)

      val result = G1Declaration.present(request)
      status(result) mustEqual OK
    }

    "add submitted form to the cached claim" in new WithApplication(app = FakeApplication(withGlobal = Some(global))) with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
        .withFormUrlEncodedBody(declarationInput: _*)

      val result = G1Declaration.submit(request)
      val claim = Cache.getAs[Claim](claimKey).get

      claim.questionGroup[CircumstancesDeclaration] must beLike {
        case Some(f: CircumstancesDeclaration) =>
          f.obtainInfoAgreement must equalTo(infoAgreement)
          f.obtainInfoWhy.get must equalTo(why)
          f.confirm must equalTo(confirm)
          f.circsSomeOneElse must equalTo(Some(someOneElse))
          f.nameOrOrganisation must equalTo(Some(nameOrOrganisation))
      }
    }

    "redirect to the next page after a valid submission" in new WithApplication(app = FakeApplication(withGlobal = Some(global))) with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
        .withFormUrlEncodedBody(declarationInput: _*)

      val result = G1Declaration.submit(request)
      redirectLocation(result) must beSome("/circs-async-submitting")
    }

  } section("unit", models.domain.CircumstancesConsentAndDeclaration.id)
}