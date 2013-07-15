package models.domain

import models._

case object Education {
  val id = "s9"
}

case class AddressOfSchoolCollegeOrUniversity(nameOfSchoolCollegeOrUniversity: Option[String],
    nameOfMainTeacherOrTutor: Option[String],
    address: Option[MultiLineAddress],
    postcode: Option[String], 
    phoneNumber: Option[String] = None,
    faxNumber: Option[String] = None) extends QuestionGroup(AddressOfSchoolCollegeOrUniversity.id)

case object AddressOfSchoolCollegeOrUniversity extends QuestionGroup(s"${Education.id}.g2")