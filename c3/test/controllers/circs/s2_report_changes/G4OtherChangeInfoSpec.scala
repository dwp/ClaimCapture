package controllers.circs.s2_report_changes

import play.api.test.FakeRequest
import models.domain.{Claim, CircumstancesOtherInfo, MockForm}
import models.view.CachedChangeOfCircs
import play.api.cache.Cache
import play.api.test.Helpers._
import org.specs2.mutable._
import controllers.circs.s2_report_changes
import utils.WithApplication

class G4OtherChangeInfoSpec extends Specification{

  val otherInfo = "other info"

  val otherChangeInfoInput = Seq("changeInCircs" -> otherInfo)

  "Circumstances - OtherChangeInfo - Controller" should {

    "present 'Other Change Information' " in new WithApplication with MockForm {
      val request = FakeRequest()

      val result = G4OtherChangeInfo.present(request)
      status(result) mustEqual OK
    }


    "add submitted form to the cached claim" in new WithApplication with MockForm {
      val request = FakeRequest()
        .withFormUrlEncodedBody(otherChangeInfoInput: _*)

      val result = s2_report_changes.G4OtherChangeInfo.submit(request)
      val claim = getClaimFromCache(result,CachedChangeOfCircs.key)
      claim.questionGroup[CircumstancesOtherInfo] must beLike {
        case Some(f: CircumstancesOtherInfo) => {
          f.change must equalTo(otherInfo)
        }
      }
    }

    "redirect to the next page after a valid submission" in new WithApplication with MockForm {
      val request = FakeRequest()
        .withFormUrlEncodedBody(otherChangeInfoInput: _*)

      val result = s2_report_changes.G4OtherChangeInfo.submit(request)
      status(result) mustEqual SEE_OTHER
    }

  }
  section("unit", models.domain.CircumstancesOtherInfo.id)

}
