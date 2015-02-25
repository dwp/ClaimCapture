package controllers.circs.s3_consent_and_declaration

import models.domain._
import models.view.CachedChangeOfCircs
import org.specs2.mutable.{Specification, Tags}
import play.api.test.Helpers._
import play.api.test.{FakeApplication, FakeRequest, WithApplication}
import services.submission.MockInjector

class G1DeclarationSpec extends Specification with MockInjector with Tags {

  val byPost = "By Post"
  val infoAgreement = "yes"
  val why = "Cause i want"
  val confirm = "yes"
  val someOneElse = "checked"
  val nameOrOrganisation = "Tesco"
  val wantsEmailContact = "no"

  val declarationInput = Seq(
    "furtherInfoContact" -> byPost,
    "obtainInfoAgreement" -> infoAgreement,
    "obtainInfoWhy" -> why,
    "confirm" -> confirm,
    "circsSomeOneElse" -> someOneElse,
    "nameOrOrganisation" -> nameOrOrganisation,
    "wantsEmailContact" -> wantsEmailContact)
  val declartionInputWithoutSomeOne = Seq("furtherInfoContact" -> byPost, "obtainInfoAgreement" -> infoAgreement, "obtainInfoWhy" -> why, "confirm" -> confirm, "circsSomeOneElse" -> "")

  val G1Declaration = resolve(classOf[G1Declaration])

  "Circumstances - OtherChangeInfo - Controller" should {

    "present 'Other Change Information' " in new WithApplication(app = FakeApplication(withGlobal = Some(global))) with MockForm {
      val request = FakeRequest()

      val result = G1Declaration.present(request)
      status(result) mustEqual OK
    }

    "add submitted form to the cached claim" in new WithApplication(app = FakeApplication(withGlobal = Some(global))) with MockForm {
      val request = FakeRequest()
        .withFormUrlEncodedBody(declarationInput: _*)

      val result = G1Declaration.submit(request)
      val claim = getClaimFromCache(result,CachedChangeOfCircs.key)

      claim.questionGroup[CircumstancesDeclaration] must beLike {
        case Some(f: CircumstancesDeclaration) =>
          f.obtainInfoAgreement must equalTo(infoAgreement)
          f.obtainInfoWhy.get must equalTo(why)
          f.confirm must equalTo(confirm)
          f.circsSomeOneElse must equalTo(Some(someOneElse))
          f.nameOrOrganisation must equalTo(Some(nameOrOrganisation))
          f.wantsContactEmail must beSome(wantsEmailContact)
      }
    }

    "redirect to the next page after a valid submission" in new WithApplication(app = FakeApplication(withGlobal = Some(global))) with MockForm {
      val request = FakeRequest()
        .withFormUrlEncodedBody(declarationInput: _*)

      val result = G1Declaration.submit(request)
      redirectLocation(result) must beSome("/circs-async-submitting")
    }

  } section("unit", models.domain.CircumstancesConsentAndDeclaration.id)
}