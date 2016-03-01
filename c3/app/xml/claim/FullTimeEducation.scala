package xml.claim

import controllers.mappings.Mappings
import models.domain._
import app.XMLValues._
import scala.xml.NodeSeq
import xml.XMLHelper._
import xml.XMLComponent
import models.domain.Claim

object FullTimeEducation extends XMLComponent {
  val datePattern = "dd-MM-yyyy"
  def xml(claim: Claim) = {
    val courseDetails = claim.questionGroup[YourCourseDetails].getOrElse(YourCourseDetails(beenInEducationSinceClaimDate = no))
    val hasBeenInEducation = courseDetails.beenInEducationSinceClaimDate == yes

    if (hasBeenInEducation) {
      <FullTimeEducation>
        {courseDetailsXml(claim)}
        {locationDetailsXml(claim)}
      </FullTimeEducation>
    } else NodeSeq.Empty
  }

  def courseDetailsXml(claim: Claim) = {
    val courseDetails = claim.questionGroup[YourCourseDetails].getOrElse(YourCourseDetails(beenInEducationSinceClaimDate = no))

    <CourseDetails>
      {/*TODO: Remove courseType from the new schema.*/}
      {question(<Title/>,"courseTitle",courseDetails.title)}
      {question(<DateStarted/>, "startDate", courseDetails.startDate)}
      {/*TODO: Remove finished date from the new schema.*/}
      {question(<ExpectedEndDate/>, "expectedEndDate", courseDetails.expectedEndDate)}
    </CourseDetails>
  }

  def locationDetailsXml(claim:Claim) = {
    val courseDetails = claim.questionGroup[YourCourseDetails].getOrElse(YourCourseDetails(beenInEducationSinceClaimDate = no))

    <LocationDetails>
      {question(<Name/>,"nameOfSchoolCollegeOrUniversity",courseDetails.nameOfSchoolCollegeOrUniversity)}
      {/*TODO: Remove address from the new schema*/}
      {question(<PhoneNumber/>,"courseContactNumber", courseDetails.courseContactNumber)}
      {/*TODO: Remove fax from the new schema*/}
      {/*TODO: Remove student reference number from the new schema*/}
      {question(<Tutor/>,"nameOfMainTeacherOrTutor", courseDetails.nameOfMainTeacherOrTutor)}
    </LocationDetails>
  }

  def fromXml(xml: NodeSeq, claim: Claim) : Claim = {
    claim.update(createYourDetailsFromXml(xml))
  }

  private def createYourDetailsFromXml(xml: NodeSeq) = {
    val fullTimeEducation = (xml \\ "FullTimeEducation")
    YourCourseDetails (
      beenInEducationSinceClaimDate = fullTimeEducation.isEmpty match { case false => Mappings.yes case true => Mappings.no },
      title = createStringOptional((fullTimeEducation \ "CourseDetails" \ "Title" \ "Answer").text),
      nameOfSchoolCollegeOrUniversity = createStringOptional((fullTimeEducation \ "LocationDetails" \ "Name" \ "Answer").text),
      nameOfMainTeacherOrTutor = createStringOptional((fullTimeEducation \ "LocationDetails" \ "Tutor" \ "Answer").text),
      courseContactNumber = createStringOptional((fullTimeEducation \ "LocationDetails" \ "PhoneNumber" \ "Answer").text),
      startDate = createFormattedDateOptional((fullTimeEducation \ "CourseDetails" \ "DateStarted" \ "Answer").text),
      expectedEndDate = createFormattedDateOptional((fullTimeEducation \ "CourseDetails" \ "ExpectedEndDate" \ "Answer").text)
    )
  }
}
