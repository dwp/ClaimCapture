package models.domain

import models._

case object Education {
  val id = "s6"
}

case class YourCourseDetails(courseType:Option[String], title:Option[String], startDate:Option[DayMonthYear], expectedEndDate:Option[DayMonthYear], finishedDate:Option[DayMonthYear], studentReferenceNumber:Option[String]) extends QuestionGroup(YourCourseDetails.id)

object YourCourseDetails extends QuestionGroup(s"${Education.id}.g1")


case class AddressOfSchoolCollegeOrUniversity(nameOfSchoolCollegeOrUniversity: Option[String],
                                              nameOfMainTeacherOrTutor: Option[String],
                                              address: Option[MultiLineAddress],
                                              postcode: Option[String],
                                              phoneNumber: Option[String] = None,
                                              faxNumber: Option[String] = None) extends QuestionGroup(AddressOfSchoolCollegeOrUniversity.id)

case object AddressOfSchoolCollegeOrUniversity extends QuestionGroup(s"${Education.id}.g2")
