package xml

import models.domain._
import XMLHelper._

object FullTimeEducation {

 def xml(claim: Claim) = {
   val courseDetailsOption = claim.questionGroup[YourCourseDetails]
   val addressOfSchoolOption = claim.questionGroup[AddressOfSchoolCollegeOrUniversity]

    <FullTimeEducation>
      {courseDetailsXml(courseDetailsOption)}
      {locationDetailsXml(addressOfSchoolOption, courseDetailsOption)}
    </FullTimeEducation>
  }

  def courseDetailsXml(courseDetailsOption: Option[YourCourseDetails]) = {
    val courseDetails = courseDetailsOption.getOrElse(YourCourseDetails())

    <CourseDetails>
      <Type>{courseDetails.courseType.orNull}</Type>
      <Title>{courseDetails.title.orNull}</Title>
      <HoursSpent></HoursSpent>
      <DateStarted>{stringify(courseDetails.startDate)}</DateStarted>
      <DateStopped>{stringify(courseDetails.finishedDate)}</DateStopped>
      <ExpectedEndDate>{stringify(courseDetails.expectedEndDate)}</ExpectedEndDate>
    </CourseDetails>
  }

  def locationDetailsXml(schoolDataOption: Option[AddressOfSchoolCollegeOrUniversity], courseDetailsOption: Option[YourCourseDetails]) = {
    val schoolData = schoolDataOption.getOrElse(AddressOfSchoolCollegeOrUniversity())
    val courseDetails = courseDetailsOption.getOrElse(YourCourseDetails())

    <LocationDetails>
      <Name>{schoolData.nameOfSchoolCollegeOrUniversity.orNull}</Name>
      <Address>{postalAddressStructure(schoolData.address, schoolData.postcode)}</Address>
      <PhoneNumber>{schoolData.phoneNumber.orNull}</PhoneNumber>
      <FaxNumber>{schoolData.faxNumber.orNull}</FaxNumber>
      <StudentReferenceNumber>{courseDetails.studentReferenceNumber.orNull}</StudentReferenceNumber>
      <Tutor>{schoolData.nameOfMainTeacherOrTutor.orNull}</Tutor>
    </LocationDetails>
  }
}