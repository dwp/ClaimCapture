package controllers.circs.s3_consent_and_declaration

import play.api.test.{FakeRequest, WithApplication}
import models.domain._
import models.view.CachedCircs
import play.api.cache.Cache
import play.api.test.Helpers._
import org.specs2.mutable.{Tags, Specification}
import scala.Some


class G1DeclarationSpec extends Specification with Tags{

  val infoAgreement = "yes"
  val why = "Cause i want"
  val confirm = "yes"

  val otherChangeInfoInput = Seq("obtainInfoAgreement" -> infoAgreement, "obtainInfoWhy" -> why, "confirm" -> confirm)

  "Circumstances - OtherChangeInfo - Controller" should {

    "present 'Other Change Information' " in new WithApplication with MockForm {
      val request = FakeRequest().withSession(CachedCircs.key -> claimKey)

      val result = controllers.circs.s3_consent_and_declaration.G1Declaration.present(request)
      status(result) mustEqual OK
    }


    "add submitted form to the cached claim" in new WithApplication with MockForm {
      val request = FakeRequest().withSession(CachedCircs.key -> claimKey)
        .withFormUrlEncodedBody(otherChangeInfoInput: _*)

      val result = controllers.circs.s3_consent_and_declaration.G1Declaration.submit(request)
      val claim = Cache.getAs[DigitalForm](claimKey).get
      claim.questionGroup[CircumstancesDeclaration] must beLike {
        case Some(f: CircumstancesDeclaration) => {
          f.obtainInfoAgreement must equalTo(infoAgreement)
          f.obtainInfoWhy.get must equalTo(why)
          f.confirm must equalTo(confirm)
        }
      }
    }

    "redirect to the next page after a valid submission" in new WithApplication with MockForm {
      val request = FakeRequest().withSession(CachedCircs.key -> claimKey)
        .withFormUrlEncodedBody(otherChangeInfoInput: _*)

      val result = controllers.circs.s3_consent_and_declaration.G1Declaration.submit(request)
      pending("until declaration points the submision result page")
      status(result) mustEqual SEE_OTHER
    }

  } section("unit", models.domain.CircumstancesConsentAndDeclaration.id)

}
