package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import org.specs2.mock.Mockito
import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import models.domain.Claiming

class G6RepresentativesForThePersonSpec extends Specification with Mockito with Tags {

  val representativesForThePersonInput = Seq("you.actForPerson" -> "no", "someoneElse.actForPerson" -> "no")

  "Representatives for the person" should {

    """present "Representatives for the Person you care for""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val result = G6RepresentativesForThePerson.present(request)
      status(result) mustEqual OK
    }

    "fail submit for no input" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val result = G6RepresentativesForThePerson.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "success for minimal input without optional fields" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey).withFormUrlEncodedBody(representativesForThePersonInput: _*)

      val result = G6RepresentativesForThePerson.submit(request)
      status(result) mustEqual SEE_OTHER
    }
  } section("unit", models.domain.CareYouProvide.id)
}