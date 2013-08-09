package controllers.s7_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import models.domain._

class G14JobCompletionSpec extends Specification with Tags {
  "Job completion" should {
    "present" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
      val result = G14JobCompletion.present("Dummy job ID")(request)
      status(result) mustEqual OK
    }

    """progress to "employment completion".""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
      val result = G14JobCompletion.submit(request)
      status(result) mustEqual SEE_OTHER
    }
  } section("unit", models.domain.Employed.id)
}