package controllers.circs.report_changes

import play.api.test.FakeRequest
import models.domain._
import models.view.CachedChangeOfCircs
import play.api.test.Helpers._
import org.specs2.mutable._
import utils.WithApplication

class GPermanentlyStoppedCaringSpec extends Specification {
  val moreAboutChanges = "more about the change"
  val stoppedCaringDateDay = 23
  val stoppedCaringDateMonth = 12
  val stoppedCaringDateYear = 2013

  val stoppedCaringInput = Seq("moreAboutChange" -> moreAboutChanges,
    "stoppedCaringDate.day" -> stoppedCaringDateDay.toString,
    "stoppedCaringDate.month" -> stoppedCaringDateMonth.toString,
    "stoppedCaringDate.year" -> stoppedCaringDateYear.toString
  )

  section("unit", models.domain.CircumstancesStoppedCaring.id)
  "Circumstances - PermanentlyStoppedCaring - Controller" should {
    "present 'Permanently Stopped Caring' " in new WithApplication with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)

      val result = GPermanentlyStoppedCaring.present(request)
      status(result) mustEqual OK
    }

    "redirect to the next page after a valid submission" in new WithApplication with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
        .withFormUrlEncodedBody(stoppedCaringInput: _*)

      val result = GPermanentlyStoppedCaring.submit(request)
      status(result) mustEqual SEE_OTHER
    }
  }
  section("unit", models.domain.CircumstancesStoppedCaring.id)
}
