package xml

import models.domain._
import XMLHelper._
import controllers.Mappings._
import scala.xml.NodeSeq

object FullTimeEducation {

 def xml(claim: Claim) = {
   val moreAboutYouOption = claim.questionGroup[MoreAboutYou]
   val moreAboutYou = moreAboutYouOption.getOrElse(MoreAboutYou(beenInEducationSinceClaimDate = no))

   val courseDetailsOption = claim.questionGroup[YourCourseDetails]
   val addressOfSchoolOption = claim.questionGroup[AddressOfSchoolCollegeOrUniversity]

   val hasBeenInEducation = moreAboutYou.beenInEducationSinceClaimDate == yes

   if(hasBeenInEducation) {
    <FullTimeEducation>
      {courseDetailsXml(courseDetailsOption)}
      {locationDetailsXml(addressOfSchoolOption, courseDetailsOption)}
    </FullTimeEducation>
   } else NodeSeq.Empty
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