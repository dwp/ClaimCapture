package controllers.circs.s3_consent_and_declaration

import models.domain._
import play.api.Play.current
import models.view.CachedChangeOfCircs
import org.specs2.mutable._
import play.api.test.Helpers._
import play.api.test.FakeRequest
import utils.{WithApplication, LightFakeApplication}

class G1DeclarationSpec extends Specification {

  val byPost = "By Post"
  val infoAgreement = "yes"
  val why = "Cause i want"
  val someOneElse = "checked"
  val nameOrOrganisation = "Tesco"
  val wantsEmailContact = "no"

  val declarationInput = Seq(
    "furtherInfoContact" -> byPost,
    "obtainInfoAgreement" -> infoAgreement,
    "obtainInfoWhy" -> why,
    "circsSomeOneElse" -> someOneElse,
    "nameOrOrganisation" -> nameOrOrganisation,
    "wantsEmailContactCircs" -> wantsEmailContact)
  val declartionInputWithoutSomeOne = Seq("furtherInfoContact" -> byPost, "obtainInfoAgreement" -> infoAgreement, "obtainInfoWhy" -> why, "circsSomeOneElse" -> "")

  "Circumstances - OtherChangeInfo - Controller" should {
    "present 'Other Change Information' " in new WithApplication(app = LightFakeApplication.fa) with MockForm {
      val G1Declaration = current.injector.instanceOf[G1Declaration]
      val request = FakeRequest()

      val result = G1Declaration.present(request)
      status(result) mustEqual OK
    }

    "add submitted form to the cached claim" in new WithApplication(app = LightFakeApplication.fa) with MockForm {
      val G1Declaration = current.injector.instanceOf[G1Declaration]
      val request = FakeRequest()
        .withFormUrlEncodedBody(declarationInput: _*)

      val result = G1Declaration.submit(request)
      val claim = getClaimFromCache(result,CachedChangeOfCircs.key)

      claim.questionGroup[CircumstancesDeclaration] must beLike {
        case Some(f: CircumstancesDeclaration) =>
          f.obtainInfoAgreement must equalTo(infoAgreement)
          f.obtainInfoWhy.get must equalTo(why)
          f.circsSomeOneElse must equalTo(Some(someOneElse))
          f.nameOrOrganisation must equalTo(Some(nameOrOrganisation))
      }
    }

    "redirect to the next page after a valid submission" in new WithApplication(app = LightFakeApplication.fa) with MockForm {
      val G1Declaration = current.injector.instanceOf[G1Declaration]
      val request = FakeRequest()
        .withFormUrlEncodedBody(declarationInput: _*)

      val result = G1Declaration.submit(request)
      redirectLocation(result) must beSome("/circs-async-submitting")
    }

  }
  section("unit", models.domain.CircumstancesConsentAndDeclaration.id)
}
