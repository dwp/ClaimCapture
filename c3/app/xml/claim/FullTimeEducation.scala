package xml.claim

import models.domain._
import scala.xml.NodeSeq
import app.XMLValues._

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
      <Type>{NotAsked}</Type>
      <Title>{courseDetails.title}</Title>
      <HoursSpent></HoursSpent>
      <DateStarted>{courseDetails.startDate.`yyyy-MM-dd`}</DateStarted>
      <DateStopped></DateStopped>
      <ExpectedEndDate>{courseDetails.expectedEndDate.`yyyy-MM-dd`}</ExpectedEndDate>
    </CourseDetails>
  }

  def locationDetailsXml(claim:Claim) = {
    val schoolData = claim.questionGroup[YourCourseDetails].getOrElse(YourCourseDetails())
    val courseDetails = claim.questionGroup[YourCourseDetails].getOrElse(YourCourseDetails())

    <LocationDetails>
      <Name>{courseDetails.nameOfSchoolCollegeOrUniversity}</Name>
      <Address>
        <gds:Line>{NotAsked}</gds:Line>
        <gds:Line>{NotAsked}</gds:Line>
        <gds:Line>{NotAsked}</gds:Line>
        <gds:PostCode></gds:PostCode>
      </Address>
      <PhoneNumber>{courseDetails.courseContactNumber.orNull}</PhoneNumber>
      <FaxNumber>{NotAsked}</FaxNumber>
      <StudentReferenceNumber>{NotAsked}</StudentReferenceNumber>
      <Tutor>{courseDetails.nameOfMainTeacherOrTutor}</Tutor>
    </LocationDetails>
  }
}