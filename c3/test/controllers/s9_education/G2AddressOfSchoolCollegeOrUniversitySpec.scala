package controllers.s9_education

import org.specs2.mutable.{Tags, Specification}
import org.specs2.mock.Mockito
import play.api.test.{WithApplication, FakeRequest}
import play.api.cache.Cache
import models.domain.{Claiming, Claim, Section, TheirContactDetails}
import play.api.test.Helpers._
import models.domain.AddressOfSchoolCollegeOrUniversity
import models.{MultiLineAddress, domain}

class G2AddressOfSchoolCollegeOrUniversitySpec extends Specification with Mockito with Tags {
  "Address of school, college or university - Controller" should {
    val nameOfSchoolCollegeOrUniversity = "MIT"
    val nameOfMainTeacherOrTutor = "Albert Einstein"
    val addressLineOne = "123 Street"
    val postcode: String = "SE1 6EH"
    val phoneNumber = "02076541058"
    val faxNumber = "07076541058"
      
    val addressOfSchoolCollegeOrUniversityInput = Seq("nameOfSchoolCollegeOrUniversity" -> nameOfSchoolCollegeOrUniversity,
      "nameOfMainTeacherOrTutor" -> nameOfMainTeacherOrTutor,
      "address.lineOne" -> addressLineOne,
      "address.lineTwo" -> "",
      "address.lineThree" -> "",
      "postcode" -> postcode,
      "phoneNumber" -> phoneNumber,
      "faxNumber" -> faxNumber)

    "present 'Address Of School College Or University'" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val result = controllers.s9_education.G2AddressOfSchoolCollegeOrUniversity.present(request)
      status(result) mustEqual OK
    }
    
    "add their contect details to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(addressOfSchoolCollegeOrUniversityInput: _*)

      val result = controllers.s9_education.G2AddressOfSchoolCollegeOrUniversity.submit(request)
      val claim = Cache.getAs[Claim](claimKey).get
      val section: Section = claim.section(domain.Education.id)

      section.questionGroup(AddressOfSchoolCollegeOrUniversity) must beLike {
        case Some(f: AddressOfSchoolCollegeOrUniversity) => {
          f.nameOfSchoolCollegeOrUniversity mustEqual Some(nameOfSchoolCollegeOrUniversity)
          f.nameOfMainTeacherOrTutor mustEqual Some(nameOfMainTeacherOrTutor)
          f.address must equalTo(Some(MultiLineAddress(Some(addressLineOne), None, None)))
          f.postcode mustEqual Some(postcode)
          f.phoneNumber mustEqual Some(phoneNumber)
          f.faxNumber mustEqual Some(faxNumber)
        }
        case _ => "This mapping should not happen, check the submit is updating claim with the correct form." must equalTo("Error")
      }
    }
    
    "return a BadRequest on an invalid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody("postcode" -> "INVALID")

      val result = controllers.s9_education.G2AddressOfSchoolCollegeOrUniversity.submit(request)
      status(result) mustEqual BAD_REQUEST
    }
    
    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(addressOfSchoolCollegeOrUniversityInput: _*)

      val result = controllers.s9_education.G2AddressOfSchoolCollegeOrUniversity.submit(request)
      status(result) mustEqual SEE_OTHER
    }
    
    "redirect to the next page after a valid submission with minimal input without optional fields" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val result = controllers.s9_education.G2AddressOfSchoolCollegeOrUniversity.submit(request)
      status(result) mustEqual SEE_OTHER
    }
  } section "unit"
}