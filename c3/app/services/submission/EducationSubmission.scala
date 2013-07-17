package services.submission

import models.domain._
import Helper.stringify
import Helper.addressXml

object EducationSubmission {

 def xml(education:Section) = {

    val courseDetailsOption = education.questionGroup(YourCourseDetails).asInstanceOf[Option[YourCourseDetails]]

    val addressOfSchoolOption = education.questionGroup(AddressOfSchoolCollegeOrUniversity).asInstanceOf[Option[AddressOfSchoolCollegeOrUniversity]]

    <FullTimeEducation>
      {courseDetailsXml(courseDetailsOption)}
      {locationDetailsXml(addressOfSchoolOption, courseDetailsOption)}
    </FullTimeEducation>
  }

  def courseDetailsXml(courseDetailsOption:Option[YourCourseDetails]):scala.xml.Elem = {

    def xml(courseDetails:YourCourseDetails) = {
      <CourseDetails>
        <Type>{stringify(courseDetails.courseType)}</Type>
        <Title>{stringify(courseDetails.title)}</Title>
        <HoursSpent></HoursSpent>
        <DateStarted>{stringify(courseDetails.startDate)}</DateStarted>
        <DateStopped>{stringify(courseDetails.finishedDate)}</DateStopped>
        <ExpectedEndDate>{stringify(courseDetails.expectedEndDate)}</ExpectedEndDate>
      </CourseDetails>
    }

    courseDetailsOption match {
      case Some(details:YourCourseDetails) => xml(details)
      case _ =>  xml(YourCourseDetails(None, None, None, None, None, None))
    }
  }

  def locationDetailsXml(schoolDataOption:Option[AddressOfSchoolCollegeOrUniversity], courseDetailsOption:Option[YourCourseDetails]) = {

    def xml(schoolData:AddressOfSchoolCollegeOrUniversity, detailsOption:Option[YourCourseDetails]) = {
      <LocationDetails>
        <Name>{stringify(schoolData.nameOfSchoolCollegeOrUniversity)}</Name>
        {addressXml(schoolData.address, schoolData.postcode)}
        <PhoneNumber>{stringify(schoolData.phoneNumber)}</PhoneNumber>
        <FaxNumber>{stringify(schoolData.faxNumber)}</FaxNumber>
        <StudentReferenceNumber>{if(detailsOption.isDefined) Helper.stringify(detailsOption.get.studentReferenceNumber)}</StudentReferenceNumber>
        <Tutor>{stringify(schoolData.nameOfMainTeacherOrTutor)}</Tutor>
      </LocationDetails>
    }

    schoolDataOption match {
      case Some(schoolData:AddressOfSchoolCollegeOrUniversity) => xml(schoolData, courseDetailsOption)
      case _ => xml(AddressOfSchoolCollegeOrUniversity(None, None, None, None), courseDetailsOption)
    }
  }

}