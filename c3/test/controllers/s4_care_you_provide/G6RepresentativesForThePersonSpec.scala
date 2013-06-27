package controllers.s4_care_you_provide

import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import play.api.test.{FakeRequest, WithApplication}
import models.view.Claiming
import play.api.test.Helpers._

class G6RepresentativesForThePersonSpec extends Specification with Mockito {

  val representativesForThePersonInput = Seq("actForPerson" -> "no", "someoneElseActForPerson" -> "no")

  "Representatives for the person" should {

    """present "Representatives for the person you care for""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val result = controllers.s4_care_you_provide.G6RepresentativesForThePerson.present(request)
      status(result) mustEqual OK
    }

    "fail submit for no input" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val result = controllers.s4_care_you_provide.G6RepresentativesForThePerson.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "success for minimal input without optional fields" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey).withFormUrlEncodedBody(representativesForThePersonInput: _*)

      val result = controllers.s4_care_you_provide.G6RepresentativesForThePerson.submit(request)
      status(result) mustEqual SEE_OTHER
    }
  }

}
