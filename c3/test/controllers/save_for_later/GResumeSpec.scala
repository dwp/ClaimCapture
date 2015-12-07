package controllers.save_for_later

import models.domain._
import org.specs2.mutable._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import utils.{LightFakeApplication, WithApplication}

class GResumeSpec extends Specification {

  "Resume controller" should {
    "present resume screen" in new WithApplication with Claiming {
      val request = FakeRequest()
      val result = GResume.present(request)
      status(result) mustEqual OK
    }

    "block present when switched off" in new WithApplication(app = LightFakeApplication(additionalConfiguration = Map("saveForLaterResumeEnabled" -> "false"))) with Claiming {
      val request = FakeRequest()
      val result = GResume.present(request)
      val bodyText: String = contentAsString(result)
      bodyText must contain("This service is currently switched off")
      status(result) mustEqual BAD_REQUEST
    }

    "block submit when switched off" in new WithApplication(app = LightFakeApplication(additionalConfiguration = Map("saveForLaterResumeEnabled" -> "false"))) with Claiming {
      val request = FakeRequest()
      val result = GResume.submit(request)
      val bodyText: String = contentAsString(result)
      println("bodyText:"+bodyText)
      bodyText must contain("This service is currently switched off")
      status(result) mustEqual BAD_REQUEST
    }

/*

    "return error present when switched off" in new WithApplication(app = LightFakeApplication(additionalConfiguration = Map("saveForLaterResumeEnabled" -> "false"))) with Claiming {
      val request = FakeRequest()
      val result = GResume.present(request)
      val bodyText: String = contentAsString(result)
      bodyText must contain("This service is currently switched off")
      status(result) mustEqual BAD_REQUEST
    }

    "allow submit" in new WithApplication(app = LightFakeApplication(additionalConfiguration = Map("saveForLaterResumeEnabled" -> "true"))) with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody("anyTrips" -> "yes")
      val result = GResume.submit(request)
      status(result) mustEqual OK
    }

*/
  }
  section("unit", models.domain.YourPartner.id)
}
