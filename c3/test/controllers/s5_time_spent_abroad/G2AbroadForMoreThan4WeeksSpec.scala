package controllers.s5_time_spent_abroad

import org.specs2.mutable.Specification
import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import models.domain.Claiming

class G2AbroadForMoreThan4WeeksSpec extends Specification {
  "Normal residence and current location" should {
    "present" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val result = G2AbroadForMoreThan4Weeks.present(request)
      status(result) mustEqual OK
    }
  }
}

/*
Have you been out of Great Britain with the person you care for, for more than four weeks at a time,
since [[Claim Date - 3 years]] (this is 3 years before your claim date)?
Yes / No
Mandatory
If answered 'Yes', show the "Details of trips abroad with the person you look after (4 weeks)' section directly
after this question


*/