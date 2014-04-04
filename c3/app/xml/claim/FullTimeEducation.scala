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
      {/*TODO: Remove courseType from the new schema.*/}
      {question(<Title/>,"courseTitle",courseDetails.title)}
      {question(<DateStarted/>, "startDate", courseDetails.startDate)}
      {/*TODO: Remove finished date from the new schema.*/}
      {question(<ExpectedEndDate/>, "expectedEndDate", courseDetails.expectedEndDate)}
    </CourseDetails>
  }

  def locationDetailsXml(claim:Claim) = {
    val courseDetails = claim.questionGroup[YourCourseDetails].getOrElse(YourCourseDetails())

    <LocationDetails>
      {question(<Name/>,"nameOfSchoolCollegeOrUniversity",courseDetails.nameOfSchoolCollegeOrUniversity)}
      {/*TODO: Remove address from the new schema*/}
      {question(<PhoneNumber/>,"courseContactNumber", courseDetails.courseContactNumber)}
      {/*TODO: Remove fax from the new schema*/}
      {/*TODO: Remove student reference number from the new schema*/}
      {question(<Tutor/>,"nameOfMainTeacherOrTutor", courseDetails.nameOfMainTeacherOrTutor)}
    </LocationDetails>
  }
}