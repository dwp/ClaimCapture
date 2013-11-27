package xml.claim

import models.domain._
import app.XMLValues._
import scala.xml.NodeSeq
import xml.XMLHelper._
import xml.XMLComponent
import models.domain.Claim
import scala.Some

object FullTimeEducation extends XMLComponent {

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
      {courseDetails.courseType match {
        case Some(n) => <Type>{courseDetails.courseType.orNull}</Type>
        case None => NodeSeq.Empty
      }}
      {courseDetails.title match {
        case Some(n) => <Title>{courseDetails.title.orNull}</Title>
        case None => NodeSeq.Empty
      }}
      {question(<DateStarted/>, "startDate", courseDetails.startDate)}
      {question(<DateStopped/>, "finishedDate", courseDetails.finishedDate)}
      {question(<ExpectedEndDate/>, "expectedEndDate", courseDetails.expectedEndDate)}
    </CourseDetails>
  }

  def locationDetailsXml(claim:Claim) = {
    val schoolData = claim.questionGroup[AddressOfSchoolCollegeOrUniversity].getOrElse(AddressOfSchoolCollegeOrUniversity())
    val courseDetails = claim.questionGroup[YourCourseDetails].getOrElse(YourCourseDetails())

    <LocationDetails>
      {schoolData.nameOfSchoolCollegeOrUniversity match {
        case Some(n) => <Name>{schoolData.nameOfSchoolCollegeOrUniversity.get}</Name>
        case None => NodeSeq.Empty
      }}
      {schoolData.address match {
        case Some(n) => postalAddressStructure(schoolData.address, schoolData.postcode)
        case None => NodeSeq.Empty
      }}
      {schoolData.phoneNumber match {
        case Some(n) => <PhoneNumber>{schoolData.phoneNumber.get}</PhoneNumber>
        case None => NodeSeq.Empty
      }}
      { schoolData.faxNumber match {
        case Some(n) => <FaxNumber>{schoolData.faxNumber.get}</FaxNumber>
        case None => NodeSeq.Empty
      }}
      { courseDetails.studentReferenceNumber match {
        case Some(n) => <StudentReferenceNumber>{courseDetails.studentReferenceNumber.get}</StudentReferenceNumber>
        case None => NodeSeq.Empty
      }}
      {schoolData.nameOfMainTeacherOrTutor match {
        case Some(n) => <Tutor>{schoolData.nameOfMainTeacherOrTutor.get}</Tutor>
        case None => NodeSeq.Empty
      }}
    </LocationDetails>
  }
}