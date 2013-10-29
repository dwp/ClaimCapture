package xml

import models.domain._
import XMLHelper._
import app.XMLValues._
import scala.xml.NodeSeq

object FullTimeEducation {

  def xml(claim: Claim) = {
    val moreAboutYou = claim.questionGroup[MoreAboutYou].getOrElse(MoreAboutYou(beenInEducationSinceClaimDate = no))

    val hasBeenInEducation = moreAboutYou.beenInEducationSinceClaimDate == yes

    if (hasBeenInEducation) {
      <FullTimeEducation>
        {courseDetailsXml(claim)}
        {locationDetailsXml(claim)}
      </FullTimeEducation>
    } else NodeSeq.Empty
  }

  def courseDetailsXml(claim: Claim) = {
    val courseDetails = claim.questionGroup[YourCourseDetails].getOrElse(YourCourseDetails())

    <CourseDetails>
      <Type>{courseDetails.courseType.orNull}</Type>
      <Title>{courseDetails.title.orNull}</Title>
      <DateStarted>
        <QuestionLabel>education.started?</QuestionLabel>
        <Answer>{stringify(courseDetails.startDate)}</Answer>
      </DateStarted>
      {courseDetails.finishedDate match {
        case Some(n) =>
          <DateStopped>
            <QuestionLabel>education.stopped?</QuestionLabel>
            <Answer>{stringify(courseDetails.finishedDate)}</Answer>
          </DateStopped>
        case None =>
        }
      }
      {courseDetails.expectedEndDate match {
          case Some(n) =>
            <ExpectedEndDate>
              <QuestionLabel>education.expectedEndDate?</QuestionLabel>
              <Answer>{stringify(courseDetails.expectedEndDate)}</Answer>
            </ExpectedEndDate>
          case None =>
        }
      }
    </CourseDetails>
  }

  def locationDetailsXml(claim:Claim) = {
    val schoolData = claim.questionGroup[AddressOfSchoolCollegeOrUniversity].getOrElse(AddressOfSchoolCollegeOrUniversity())
    val courseDetails = claim.questionGroup[YourCourseDetails].getOrElse(YourCourseDetails())

    <LocationDetails>
      {schoolData.nameOfSchoolCollegeOrUniversity match {
        case Some(n) => <Name>{schoolData.nameOfSchoolCollegeOrUniversity.orNull}</Name>
        case None => NodeSeq.Empty
      }}
      {postalAddressStructure(schoolData.address, schoolData.postcode)}
      <PhoneNumber>{schoolData.phoneNumber.orNull}</PhoneNumber>
      { schoolData.faxNumber match {
        case Some(n) => <FaxNumber>{schoolData.faxNumber.orNull}</FaxNumber>
        case None => NodeSeq.Empty
      }}
      { courseDetails.studentReferenceNumber match {
        case Some(n) => <StudentReferenceNumber>{courseDetails.studentReferenceNumber.orNull}</StudentReferenceNumber>
        case None => NodeSeq.Empty
      }}
      {schoolData.nameOfMainTeacherOrTutor match {
        case Some(n) => <Tutor>{schoolData.nameOfMainTeacherOrTutor.orNull}</Tutor>
        case None => NodeSeq.Empty
      }}
    </LocationDetails>
  }
}