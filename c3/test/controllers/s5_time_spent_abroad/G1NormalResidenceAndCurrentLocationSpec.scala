package controllers.s5_time_spent_abroad

import org.specs2.mutable.Specification
import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import models.domain.Claiming

class G1NormalResidenceAndCurrentLocationSpec extends Specification {
  "Normal residence and current location" should {
    "present" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val result = G1NormalResidenceAndCurrentLocation.present(request)
      status(result) mustEqual OK
    }

    """enforce answer to "Do you normally live in the UK, Republic of Ireland, Isle of Man or the Channel Islands?".""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val result = G1NormalResidenceAndCurrentLocation.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    """accept "yes" to "Do you normally live in the UK, Republic of Ireland, Isle of Man or the Channel Islands?".""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey).withFormUrlEncodedBody("normallyLiveInUK" -> "yes")

      val result = G1NormalResidenceAndCurrentLocation.submit(request)
      redirectLocation(result) must beSome("/timeSpentAbroad/abroadForMoreThan4Weeks")
    }

    """reject "no" to "Do you normally live in the UK, Republic of Ireland, Isle of Man or the Channel Islands?"
       having not answered "Where do you normally live?".""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey).withFormUrlEncodedBody("normallyLiveInUK" -> "no")

      val result = G1NormalResidenceAndCurrentLocation.submit(request)
      status(result) mustEqual BAD_REQUEST
    }
  }
}


/*
Your normal residence & current location

Do you normally live in the UK, Republic of Ireland, Isle of Man or the Channel Islands?
Yes / No
Mandatory
If answered ‘No’, show the ‘Where do you normally live’ field.

Where do you normally live?
Free text
Mandatory
Becomes enabled when question above=‘No’
Are you in Great Britain now?
Yes / No
Mandatory

*/