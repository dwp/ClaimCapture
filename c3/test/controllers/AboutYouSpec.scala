package controllers

import org.specs2.mutable.Specification
import play.api.test.{FakeRequest, WithApplication}
import play.api.cache.Cache
import models.claim._
import play.api.test.Helpers._
import models.claim.Section
import models.claim.Claim
import org.specs2.mock.Mockito

class AboutYouSpec extends Specification with Mockito {
  "About you" should {
    "accept all initial mandatory data" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(
        "firstName" -> "Scooby",
        "title" -> "Mr",
        "surname" -> "Doo",
        "nationality" -> "US",
        "dateOfBirth.day" -> "5",
        "dateOfBirth.month" -> "12",
        "dateOfBirth.year" -> "1990",
        "maritalStatus" -> "Single",
        "alwaysLivedUK" -> "yes")

      val result = AboutYou.yourDetailsSubmit(request)
      redirectLocation(result) must beSome("/aboutyou/contactDetails")

      val claim = Cache.getAs[Claim](claimKey).get
      val section: Section = claim.section(models.claim.AboutYou.id).get

      section.form(YourDetails.id) must beLike {
        case Some(f: YourDetails) => f.firstName mustEqual "Scooby"
      }
    }

    "highlight missing mandatory data" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody("firstName" -> "Scooby", "action" -> "next")

      val result = AboutYou.yourDetailsSubmit(request)
      status(result) mustEqual BAD_REQUEST

      val claim = Cache.getAs[Claim](claimKey).get
      claim.section(models.claim.AboutYou.id) must beNone
    }

    """present completion""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val result = AboutYou.completed(request)
      status(result) mustEqual OK
    }

    """present first "about you" page upon completing with missing forms""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val yourDetails = mock[YourDetails]
      yourDetails.id returns YourDetails.id

      val claim = Claim().update(yourDetails)

      Cache.set(claimKey, claim)

      val result = AboutYou.completedSubmit(request)
      redirectLocation(result) must beSome("/aboutyou/yourDetails")
    }

    "continue to partner/spouse upon section completion" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val yourDetails = mock[YourDetails]
      yourDetails.id returns YourDetails.id

      val contactDetails = mock[ContactDetails]
      contactDetails.id returns ContactDetails.id

      val claimDate = mock[ClaimDate]
      claimDate.id returns ClaimDate.id

      val moreAboutYou = mock[MoreAboutYou]
      moreAboutYou.id returns MoreAboutYou.id

      val employment = mock[Employment]
      employment.id returns Employment.id

      val propertyAndRent = mock[PropertyAndRent]
      propertyAndRent.id returns PropertyAndRent.id

      val claim = Claim()
        .update(yourDetails)
        .update(contactDetails)
        .update(claimDate)
        .update(moreAboutYou)
        .update(employment)
        .update(propertyAndRent)

      Cache.set(claimKey, claim)

      val result = AboutYou.completedSubmit(request)
      redirectLocation(result) must beSome("/yourpartner/yourpartner")
    }
  }
}