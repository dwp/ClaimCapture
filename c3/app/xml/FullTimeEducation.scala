package xml

import models.domain._
import XMLHelper._
import scala.Some

object FullTimeEducation {

 def xml(claim:Claim) = {

   val education = claim.section(Education)

   val courseDetailsOption = questionGroup[YourCourseDetails](education, YourCourseDetails)

    val addressOfSchoolOption = questionGroup[AddressOfSchoolCollegeOrUniversity](education, AddressOfSchoolCollegeOrUniversity )

    <FullTimeEducation>
      {courseDetailsXml(courseDetailsOption)}
      {locationDetailsXml(addressOfSchoolOption, courseDetailsOption)}
    </FullTimeEducation>
  }

  def courseDetailsXml(courseDetailsOption:Option[YourCourseDetails]) = {

    def xml(courseDetails:YourCourseDetails) = {
      <CourseDetails>
        <Type>{courseDetails.courseType.orNull}</Type>
        <Title>{courseDetails.title.orNull}</Title>
        <HoursSpent></HoursSpent>
        <DateStarted>{stringify(courseDetails.startDate)}</DateStarted>
        <DateStopped>{stringify(courseDetails.finishedDate)}</DateStopped>
        <ExpectedEndDate>{stringify(courseDetails.expectedEndDate)}</ExpectedEndDate>
      </CourseDetails>
    }

    courseDetailsOption match {
      case Some(details:YourCourseDetails) => xml(details)
      case _ => xml(YourCourseDetails(NoRouting, None, None, None, None, None, None))
    }
  }

  def locationDetailsXml(schoolDataOption:Option[AddressOfSchoolCollegeOrUniversity], courseDetailsOption:Option[YourCourseDetails]) = {

    def xml(schoolData:AddressOfSchoolCollegeOrUniversity, detailsOption:Option[YourCourseDetails]) = {
      <LocationDetails>
        <Name>{schoolData.nameOfSchoolCollegeOrUniversity.orNull}</Name>
        <Address>{postalAddressStructure(schoolData.address, schoolData.postcode)}</Address>
        <PhoneNumber>{schoolData.phoneNumber.orNull}</PhoneNumber>
        <FaxNumber>{schoolData.faxNumber.orNull}</FaxNumber>
        <StudentReferenceNumber>{if(detailsOption.isDefined) detailsOption.get.studentReferenceNumber.orNull}</StudentReferenceNumber>
        <Tutor>{schoolData.nameOfMainTeacherOrTutor.orNull}</Tutor>
      </LocationDetails>
    }

    schoolDataOption match {
      case Some(schoolData:AddressOfSchoolCollegeOrUniversity) => xml(schoolData, courseDetailsOption)
      case _ => xml(AddressOfSchoolCollegeOrUniversity(NoRouting, None, None, None, None), courseDetailsOption)
    }
  }

}