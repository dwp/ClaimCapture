package controllers.circs.report_changes

import play.api.test.FakeRequest
import models.domain.{CircumstancesOtherInfo, MockForm}
import models.view.CachedChangeOfCircs
import play.api.test.Helpers._
import org.specs2.mutable._
import utils.WithApplication

class GOtherChangeInfoSpec extends Specification{

  val otherInfo = "other info"

  val otherChangeInfoInput = Seq("changeInCircs" -> otherInfo)

  section("unit", models.domain.CircumstancesOtherInfo.id)
  "Circumstances - OtherChangeInfo - Controller" should {
    "present 'Other Change Information' " in new WithApplication with MockForm {
      val request = FakeRequest()

      val result = GOtherChangeInfo.present(request)
      status(result) mustEqual OK
    }

    "add submitted form to the cached claim" in new WithApplication with MockForm {
      val request = FakeRequest()
        .withFormUrlEncodedBody(otherChangeInfoInput: _*)

      val result = GOtherChangeInfo.submit(request)
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

      val result = GOtherChangeInfo.submit(request)
      status(result) mustEqual SEE_OTHER
    }

  }
  section("unit", models.domain.CircumstancesOtherInfo.id)
}
