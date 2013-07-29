package services.submission

import org.specs2.mutable.Specification
import org.specs2.mutable.Tags
import models.domain._
import models.DayMonthYear
import models.MultiLineAddress
import scala.Some

class EducationSubmissionSpec extends Specification with Tags {

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


  "Education Submission" should {
    "generate xml" in {

      val yourCourseDetails = YourCourseDetails(NoRouting, courseType, courseTitle, startDate, expectedEndDate, finishedDate, studentRefNr)
      val addressOfSchool = AddressOfSchoolCollegeOrUniversity(NoRouting, schoolName, tutorName, address, postcode, phoneNumber, faxNumber)
      val educationXml = EducationSubmission.xml(Section(Education, yourCourseDetails :: addressOfSchool :: Nil))

      val courseDetailsXml = educationXml \\ "FullTimeEducation" \\ "CourseDetails"
      (courseDetailsXml \\ "Type").text mustEqual courseType.get
      (courseDetailsXml \\ "Title").text mustEqual courseTitle.get
      (courseDetailsXml \\ "DateStarted").text mustEqual startDate.get.toXmlString
      (courseDetailsXml \\ "DateStopped").text mustEqual finishedDate.get.toXmlString
      (courseDetailsXml \\ "ExpectedEndDate").text mustEqual expectedEndDate.get.toXmlString

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

  } section "unit"

}
