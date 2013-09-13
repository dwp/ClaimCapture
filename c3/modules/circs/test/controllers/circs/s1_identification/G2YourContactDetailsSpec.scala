package controllers.circs.s1_identification

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeRequest, WithApplication}
import models.domain._
import models.view.CachedCircs
import play.api.test.Helpers._
import play.api.cache.Cache
import scala.Some


class G2YourContactDetailsSpec extends Specification with Tags{

  "Circumstances - Your contact details - Controller" should {

    val addressLineOne = "one"
    val addressLineTwo = "two"
    val addressLineThree = "three"

    val postCode = "PR2 8AE"
    val phoneNumber = "1234466"
    val mobileNumber = "444444"

    val yourContactDetailsInput = Seq("address.lineOne" -> addressLineOne,
      "address.lineTwo" -> addressLineTwo,
      "address.lineThree" -> addressLineThree,
      "postcode" -> postCode,
      "phoneNumber" -> phoneNumber,
      "mobileNumber" -> mobileNumber
    )

    "present 'Circumstances Your contact details' " in new WithApplication with MockForm {
      val request = FakeRequest().withSession(CachedCircs.key -> claimKey)

      val result = controllers.circs.s1_identification.G2YourContactDetails.present(request)
      status(result) mustEqual OK
    }


    "add submitted form to the cached claim" in new WithApplication with MockForm {
      val request = FakeRequest().withSession(CachedCircs.key -> claimKey)
        .withFormUrlEncodedBody(yourContactDetailsInput: _*)

      val result = controllers.circs.s1_identification.G2YourContactDetails.submit(request)
      val claim = Cache.getAs[DigitalForm](claimKey).get
      val section: Section = claim.section(models.domain.CircumstancesIdentification)
      section.questionGroup(CircumstancesYourContactDetails) must beLike {
        case Some(f: CircumstancesYourContactDetails) => {
          f.address.lineOne must equalTo(Some(addressLineOne))
          f.address.lineTwo must equalTo(Some(addressLineTwo))
          f.address.lineThree must equalTo(Some(addressLineThree))
          f.postcode must equalTo(Some(postCode))
          f.phoneNumber must equalTo(Some(phoneNumber))
          f.mobileNumber must equalTo(Some(mobileNumber))
        }
      }
    }


    "missing mandatory field" in new WithApplication with MockForm {
      val request = FakeRequest().withSession(CachedCircs.key -> claimKey)
        .withFormUrlEncodedBody("postcode" -> "")

      val result = controllers.circs.s1_identification.G2YourContactDetails.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "redirect to the next page after a valid submission" in new WithApplication with MockForm {
      val request = FakeRequest().withSession(CachedCircs.key -> claimKey)
        .withFormUrlEncodedBody(yourContactDetailsInput: _*)

      val result = controllers.circs.s1_identification.G2YourContactDetails.submit(request)
      status(result) mustEqual SEE_OTHER
    }

  } section("unit", models.domain.CircumstancesYourContactDetails.id)
}
