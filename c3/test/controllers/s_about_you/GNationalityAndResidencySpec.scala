package controllers.s_about_you

import org.mockito.Matchers
import org.specs2.mutable._
import play.api.test.FakeRequest
import models.domain.Claiming
import models.view.CachedClaim
import play.api.test.Helpers._
import utils.WithApplication

class GNationalityAndResidencySpec extends Specification {
  section("unit", models.domain.AboutYou.id)
  "Your nationality and residency" should {
    """present Your nationality and residency""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
      val result = GNationalityAndResidency.present(request)
      status(result) mustEqual OK
    }

    """block submit for no input""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
      val result = GNationalityAndResidency.submit(request)
      status(result) mustEqual BAD_REQUEST
      println("redriected back to start with url:" + redirectLocation(result))
    }

    """success for British national living UK with no trips""" in new WithApplication with Claiming {
      val input = Seq("nationality" -> "British", "alwaysLivedInUK" -> "yes", "trip52weeks" -> "no")
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody(input: _*)
      val result = GNationalityAndResidency.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    """block for other national living UK with no trips""" in new WithApplication with Claiming {
      val input = Seq("nationality" -> "Another nationality", "actualnationality" -> "", "alwaysLivedInUK" -> "yes", "trip52weeks" -> "no")
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody(input: _*)
      val result = GNationalityAndResidency.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    """success for French national living UK with no trips""" in new WithApplication with Claiming {
      val input = Seq("nationality" -> "Another nationality", "actualnationality" -> "French", "alwaysLivedInUK" -> "yes", "trip52weeks" -> "no")
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody(input: _*)
      val result = GNationalityAndResidency.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    """block for always living UK no but not answered live UK now""" in new WithApplication with Claiming {
      val input = Seq("nationality" -> "British", "alwaysLivedInUK" -> "no", "trip52weeks" -> "no")
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody(input: _*)
      val result = GNationalityAndResidency.submit(request)
      println("source..." + contentAsString(result))
      status(result) mustEqual BAD_REQUEST
      contentAsString(result) must contain("You must complete this section")
    }

    """success for always living UK no and answered live UK now no""" in new WithApplication with Claiming {
      val input = Seq("nationality" -> "British", "alwaysLivedInUK" -> "no", "liveInUKNow" -> "no", "trip52weeks" -> "no")
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody(input: _*)
      val result = GNationalityAndResidency.submit(request)
      println("source..." + contentAsString(result))
      status(result) mustEqual SEE_OTHER
    }

    """block for live UK now yes but no since when""" in new WithApplication with Claiming {
      val input = Seq("nationality" -> "British", "alwaysLivedInUK" -> "no", "liveInUKNow" -> "yes", "trip52weeks" -> "no")
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody(input: _*)
      val result = GNationalityAndResidency.submit(request)
      println("source..." + contentAsString(result))
      status(result) mustEqual BAD_REQUEST
      contentAsString(result) must contain("You must complete this section")
    }

    """allow for live UK now yes for more than 3 years""" in new WithApplication with Claiming {
      val input = Seq("nationality" -> "British", "alwaysLivedInUK" -> "no", "liveInUKNow" -> "yes", "arrivedInUK" -> "more", "trip52weeks" -> "no")
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody(input: _*)
      val result = GNationalityAndResidency.submit(request)
      println("source..." + contentAsString(result))
      status(result) mustEqual SEE_OTHER
    }

    """block for live UK now yes for less than 3 years but not since date""" in new WithApplication with Claiming {
      val input = Seq("nationality" -> "British", "alwaysLivedInUK" -> "no", "liveInUKNow" -> "yes", "arrivedInUK" -> "less", "trip52weeks" -> "no")
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody(input: _*)
      val result = GNationalityAndResidency.submit(request)
      println("source..." + contentAsString(result))
      status(result) mustEqual BAD_REQUEST
      contentAsString(result) must contain("You must complete this section")
    }

    """allow for live UK now yes for less than 3 years and good since date""" in new WithApplication with Claiming {
      val arrivedInUKDate = Seq("arrivedInUKDate.day" -> "20", "arrivedInUKDate.month" -> "10", "arrivedInUKDate.year" -> "2015")
      val input = arrivedInUKDate ++ Seq("nationality" -> "British", "alwaysLivedInUK" -> "no", "liveInUKNow" -> "yes", "arrivedInUK" -> "less", "arrivedInUKDate" -> "less", "trip52weeks" -> "no")
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody(input: _*)
      val result = GNationalityAndResidency.submit(request)
      println("source..." + contentAsString(result))
      status(result) mustEqual SEE_OTHER
    }

    """block for bad date""" in new WithApplication with Claiming {
      val arrivedInUKDate = Seq("arrivedInUKDate.day" -> "32", "arrivedInUKDate.month" -> "10", "arrivedInUKDate.year" -> "2015")
      val input = arrivedInUKDate ++ Seq("nationality" -> "British", "alwaysLivedInUK" -> "no", "liveInUKNow" -> "yes", "arrivedInUK" -> "less", "arrivedInUKDate" -> "less", "trip52weeks" -> "no")
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody(input: _*)
      val result = GNationalityAndResidency.submit(request)
      status(result) mustEqual BAD_REQUEST
      contentAsString(result) must contain("Invalid value")
    }

    """block for trips yes but no info""" in new WithApplication with Claiming {
      val input = Seq("nationality" -> "British", "alwaysLivedInUK" -> "yes", "trip52weeks" -> "yes")
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody(input: _*)
      val result = GNationalityAndResidency.submit(request)
      status(result) mustEqual BAD_REQUEST
      contentAsString(result) must contain("You must complete this section")
    }

    """allow for trips yes and info added""" in new WithApplication with Claiming {
      val input = Seq("nationality" -> "British", "alwaysLivedInUK" -> "yes", "trip52weeks" -> "yes", "tripDetails" -> "lived in spain for a while")
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody(input: _*)
      val result = GNationalityAndResidency.submit(request)
      status(result) mustEqual SEE_OTHER
    }
  }
  section("unit", models.domain.AboutYou.id)
}
