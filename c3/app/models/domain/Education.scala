package models.domain

import models._
import play.api.mvc.Call

object Education extends Section.Identifier {
  val id = "s6"
}

case class YourCourseDetails(call: Call = NoRouting,
                             courseType: Option[String] = None,
                             title: Option[String] = None,
                             startDate: Option[DayMonthYear] = None,
                             expectedEndDate: Option[DayMonthYear] = None,
                             finishedDate: Option[DayMonthYear] = None,
                             studentReferenceNumber: Option[String] = None) extends QuestionGroup(YourCourseDetails)

object YourCourseDetails extends QuestionGroup.Identifier {
  val id = s"${Education.id}.g1"
}

case class AddressOfSchoolCollegeOrUniversity(call: Call = NoRouting,
                                              nameOfSchoolCollegeOrUniversity: Option[String] = None,
                                              nameOfMainTeacherOrTutor: Option[String] = None,
                                              address: Option[MultiLineAddress] = None,
                                              postcode: Option[String] = None,
                                              phoneNumber: Option[String] = None,
                                              faxNumber: Option[String] = None) extends QuestionGroup(AddressOfSchoolCollegeOrUniversity)

object AddressOfSchoolCollegeOrUniversity extends QuestionGroup.Identifier {
  val id = s"${Education.id}.g2"
}