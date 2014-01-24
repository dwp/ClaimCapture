package models.domain

import models._

object Education extends Section.Identifier {
  val id = "s6"
}

case class YourCourseDetails(title: String  = "",
                             nameOfSchoolCollegeOrUniversity: String = "",
                             nameOfMainTeacherOrTutor: String = "",
                             courseContactNumber: Option[String] = None,
                             startDate: DayMonthYear = DayMonthYear(None, None, None),
                             expectedEndDate: DayMonthYear = DayMonthYear(None, None, None)
                             ) extends QuestionGroup(YourCourseDetails)

object YourCourseDetails extends QuestionGroup.Identifier {
  val id = s"${Education.id}.g1"
}