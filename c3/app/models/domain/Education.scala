package models.domain

import models._
import play.api.mvc.Call

object Education extends Section.Identifier {
  val id = "s6"
}

case class YourCourseDetails(call: Call,
                             courseType: Option[String], title: Option[String],
                             startDate: Option[DayMonthYear], expectedEndDate: Option[DayMonthYear], finishedDate: Option[DayMonthYear],
                             studentReferenceNumber: Option[String]) extends QuestionGroup(YourCourseDetails)

object YourCourseDetails extends QuestionGroup.Identifier {
  val id = s"${Education.id}.g1"
}

case class AddressOfSchoolCollegeOrUniversity(call: Call,
                                              nameOfSchoolCollegeOrUniversity: Option[String],
                                              nameOfMainTeacherOrTutor: Option[String],
                                              address: Option[MultiLineAddress],
                                              postcode: Option[String],
                                              phoneNumber: Option[String] = None,
                                              faxNumber: Option[String] = None) extends QuestionGroup(AddressOfSchoolCollegeOrUniversity)

object AddressOfSchoolCollegeOrUniversity extends QuestionGroup.Identifier {
  val id = s"${Education.id}.g2"
}