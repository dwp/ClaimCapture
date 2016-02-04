package controllers.feedback

import controllers.save_for_later.GSaveForLater
import models.domain.Claiming
import models.view.CachedClaim
import org.specs2.mutable._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import utils.{LightFakeApplication, WithApplication}

class GFeedbackSpec extends Specification {
  val inputNoneBad = Seq()
  val inputSatisfiedOnlyOK = Seq("satisfiedAnswer" -> "VS")
  val inputYesDifficultyOnlyOK = Seq("satisfiedAnswer" -> "VS", "difficultyAndText.answer" -> "yes")
  val inputNoDifficultyOnlyOK = Seq("satisfiedAnswer" -> "VS", "difficultyAndText.answer" -> "no")
  val inputNoDifficultyAndTextOK = Seq("satisfiedAnswer" -> "VS", "difficultyAndText.answer" -> "no", "difficultyAndText.text2" -> "easy enough")
  val inputYesDifficultyAndTextOK = Seq("satisfiedAnswer" -> "VS", "difficultyAndText.answer" -> "yes", "difficultyAndText.text1" -> "loads of problems")

  section("unit", models.domain.ThirdParty.id)
  "Feedback page" should {
    "Show switched off screen instead of present() when off" in new WithApplication(app = LightFakeApplication(additionalConfiguration=Map("feedback.cads.enabled"->"false"))) with Claiming {
      val request = FakeRequest()
      val result = GFeedback.present(request)
      val bodyText: String = contentAsString(result)
      bodyText must contain("This service is currently switched off")
      status(result) mustEqual BAD_REQUEST
    }

    "Show switched off screen instead of submit() when off" in new WithApplication(app = LightFakeApplication(additionalConfiguration=Map("feedback.cads.enabled"->"false"))) with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
      val result = GFeedback.submit(request)
      val bodyText: String = contentAsString(result)
      bodyText must contain("This service is currently switched off")
      status(result) mustEqual BAD_REQUEST
    }

    "present ok in new WithApplication with Claiming" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
      val result = GFeedback.present(request)
      status(result) mustEqual OK
    }

    "fail submit for no inputs selected" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody(inputNoneBad: _*)
      val result = GFeedback.submit(request)
      val bodyText: String = contentAsString(result)
      bodyText must contain("Check the form" )
      status(result) mustEqual BAD_REQUEST
    }

    "allow submit for just satisfaction filled" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody(inputSatisfiedOnlyOK: _*)
      val result = GFeedback.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    "allow submit for satisfied filled and difficulty question yes but no text entered" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody(inputYesDifficultyOnlyOK: _*)
      val result = GFeedback.submit(request)
      println(contentAsString(result))
      status(result) mustEqual SEE_OTHER
    }

    "allow submit for satisfied filled and difficulty question no but no text entered" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody(inputNoDifficultyOnlyOK: _*)
      val result = GFeedback.submit(request)
      println(contentAsString(result))
      status(result) mustEqual SEE_OTHER
    }

    "allow submit for all inputs filled with yes difficulty" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody(inputYesDifficultyAndTextOK: _*)
      val result = GFeedback.submit(request)
      println(contentAsString(result))
      status(result) mustEqual SEE_OTHER
    }

    "allow submit for all inputs filled with no difficulty" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody(inputNoDifficultyAndTextOK: _*)
      val result = GFeedback.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    "good submit redirect to uk gov page" in new WithApplication(app = LightFakeApplication(additionalConfiguration = Map("origin.tag" -> "GB", "feedback.gb.thankyou.url" -> "/govuk/thankyou"))) with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody(inputSatisfiedOnlyOK: _*)
      val result = GFeedback.submit(request)
      status(result) mustEqual SEE_OTHER
      redirectLocation(result) must beSome("/govuk/thankyou")
    }

    "good submit for nissa redirect to ni gov page" in new WithApplication(app = LightFakeApplication(additionalConfiguration = Map("origin.tag" -> "GB-NIR", "feedback.ni.thankyou.url" -> "/nissagov/thankyou"))) with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody(inputSatisfiedOnlyOK: _*)
      val result = GFeedback.submit(request)
      status(result) mustEqual SEE_OTHER
      redirectLocation(result) must beSome("/nissagov/thankyou")
    }
  }
  section("unit", models.domain.ThirdParty.id)
}
