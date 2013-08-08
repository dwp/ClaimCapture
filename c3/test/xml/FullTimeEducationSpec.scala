package xml

import org.specs2.mutable.Specification
import org.specs2.mutable.Tags
import models.domain._
import models.DayMonthYear
import controllers.Mappings._
import models.MultiLineAddress

class FullTimeEducationSpec extends Specification with Tags {

  val courseType = Some("courseType")
  val courseTitle = Some("courseTitle")
  val startDate = Some(DayMonthYear(Some(1), Some(1), Some(2001)))
  val expectedEndDate = Some(DayMonthYear(Some(2), Some(2), Some(2002)))
  val finishedDate = Some(DayMonthYear(Some(3), Some(3), Some(2003)))
  val studentRefNr = Some("ST11")

  val schoolName = Some("schoolName")
  val tutorName = Some("tutorName")
  val address = Some(MultiLineAddress(Some("line1"), Some("line2"), Some("line3")))
  val postcode = Some("SE1 6EH")
  val phoneNumber = Some("020192827273")
  val faxNumber = Some("0302928273")

  "FullTimeEducation" should {
    "generate <FullTimeEducation> xml when claimer has been in education" in {

      val moreAboutYou = MoreAboutYou(beenInEducationSinceClaimDate = yes)
      val yourCourseDetails = YourCourseDetails(courseType, courseTitle, startDate, expectedEndDate, finishedDate, studentRefNr)
      val addressOfSchool = AddressOfSchoolCollegeOrUniversity(schoolName, tutorName, address, postcode, phoneNumber, faxNumber)
      val claim = Claim().update(moreAboutYou).update(Section(Education, yourCourseDetails :: addressOfSchool :: Nil))
      val educationXml = FullTimeEducation.xml(claim)

      val courseDetailsXml = educationXml \\ "FullTimeEducation" \\ "CourseDetails"
      (courseDetailsXml \\ "Type").text mustEqual courseType.get
      (courseDetailsXml \\ "Title").text mustEqual courseTitle.get
      (courseDetailsXml \\ "DateStarted").text mustEqual startDate.get.`yyyy-MM-dd`
      (courseDetailsXml \\ "DateStopped").text mustEqual finishedDate.get.`yyyy-MM-dd`
      (courseDetailsXml \\ "ExpectedEndDate").text mustEqual expectedEndDate.get.`yyyy-MM-dd`

      val locationDetailsXml = educationXml \\ "FullTimeEducation" \\ "LocationDetails"
      (locationDetailsXml \\ "Name").text mustEqual schoolName.get
      (locationDetailsXml \\ "Address" \\ "Line").theSeq(0).text mustEqual address.get.lineOne.get
      (locationDetailsXml \\ "Address" \\ "Line").theSeq(1).text mustEqual address.get.lineTwo.get
      (locationDetailsXml \\ "Address" \\ "Line").theSeq(2).text mustEqual address.get.lineThree.get
      (locationDetailsXml \\ "Address" \\ "PostCode").text mustEqual postcode.get
      (locationDetailsXml \\ "PhoneNumber").text mustEqual phoneNumber.get
      (locationDetailsXml \\ "FaxNumber").text mustEqual faxNumber.get
      (locationDetailsXml \\ "StudentReferenceNumber").text mustEqual studentRefNr.get
      (locationDetailsXml \\ "Tutor").text mustEqual tutorName.get
    }

    "skip <FullTimeEducation> xml when claimer has NOT been in education" in {
      val moreAboutYou = MoreAboutYou(beenInEducationSinceClaimDate = no)
      val claim = Claim().update(moreAboutYou)
      val educationXml = FullTimeEducation.xml(claim)

      educationXml.text must beEmpty
    }
  } section "unit"
}
