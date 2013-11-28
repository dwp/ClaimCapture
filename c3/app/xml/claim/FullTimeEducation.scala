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
      {statement(<Type/>,courseDetails.courseType)}
      {statement(<Title/>,courseDetails.title)}
      {question(<DateStarted/>, "startDate", courseDetails.startDate)}
      {question(<DateStopped/>, "finishedDate", courseDetails.finishedDate)}
      {question(<ExpectedEndDate/>, "expectedEndDate", courseDetails.expectedEndDate)}
    </CourseDetails>
  }

  def locationDetailsXml(claim:Claim) = {
    val schoolData = claim.questionGroup[AddressOfSchoolCollegeOrUniversity].getOrElse(AddressOfSchoolCollegeOrUniversity())
    val courseDetails = claim.questionGroup[YourCourseDetails].getOrElse(YourCourseDetails())

    <LocationDetails>
      {statement(<Name/>,schoolData.nameOfSchoolCollegeOrUniversity)}
      {schoolData.address match {
        case Some(n) => postalAddressStructure(schoolData.address, schoolData.postcode)
        case None => NodeSeq.Empty
      }}
      {statement(<PhoneNumber/>,schoolData.phoneNumber)}
      {statement(<FaxNumber/>,schoolData.faxNumber)}
      {statement(<StudentReferenceNumber/>,courseDetails.studentReferenceNumber)}
      {statement(<Tutor/>,schoolData.nameOfMainTeacherOrTutor)}
    </LocationDetails>
  }
}