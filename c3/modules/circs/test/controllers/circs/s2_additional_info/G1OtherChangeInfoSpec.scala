package controllers.circs.s2_additional_info

import play.api.test.{FakeRequest, WithApplication}
import models.domain.{CircumstancesOtherInfo, Claim, Claiming}
import models.view.CachedClaim
import play.api.cache.Cache
import play.api.test.Helpers._
import org.specs2.mutable.{Tags, Specification}


class G1OtherChangeInfoSpec extends Specification with Tags{

  val otherInfo = "other info"

  val otherChangeInfoInput = Seq("changeInCircs" -> otherInfo)

  "Circumstances - OtherChangeInfo - Controller" should {

    "present 'Other Change Information' " in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.claimKey -> claimKey)

      val result = controllers.circs.s2_additional_info.G1OtherChangeInfo.present(request)
      status(result) mustEqual OK
    }


    "add submitted form to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.claimKey -> claimKey)
        .withFormUrlEncodedBody(otherChangeInfoInput: _*)

      val result = controllers.circs.s2_additional_info.G1OtherChangeInfo.submit(request)
      val claim = Cache.getAs[Claim](claimKey).get
      claim.questionGroup[CircumstancesOtherInfo] must beLike {
        case Some(f: CircumstancesOtherInfo) => {
          f.change must equalTo(otherInfo)
        }
      }
    }

    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.claimKey -> claimKey)
        .withFormUrlEncodedBody(otherChangeInfoInput: _*)

      val result = controllers.circs.s2_additional_info.G1OtherChangeInfo.submit(request)
      status(result) mustEqual SEE_OTHER
    }

  } section("unit", models.domain.CircumstancesAdditionalInfo.id)

}
