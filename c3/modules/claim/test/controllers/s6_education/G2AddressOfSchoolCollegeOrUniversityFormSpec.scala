package controllers.s6_education

import org.specs2.mutable.{Tags, Specification}

class G2AddressOfSchoolCollegeOrUniversityFormSpec extends Specification with Tags {
  val nameOfSchoolCollegeOrUniversity = "MIT"
  val nameOfMainTeacherOrTutor = "Albert Einstein"
  val addressLineOne = "123 Street"
  val postcode: String = "SE1 6EH"
  val phoneNumber = "02076541058"
  val faxNumber = "07076541058"
      
  "Address of school, college or university" should {
    "map data into case class" in {
      G2AddressOfSchoolCollegeOrUniversity.form.bind(
        Map(
            "nameOfSchoolCollegeOrUniversity" -> nameOfSchoolCollegeOrUniversity,
            "nameOfMainTeacherOrTutor" -> nameOfMainTeacherOrTutor,
            "address.street.lineOne" -> addressLineOne,
            "address.town.lineTwo" -> "lineTwo",
            "address.town.lineThree" -> "lineThree",
            "postcode" -> postcode,
            "phoneNumber" -> phoneNumber,
            "faxNumber" -> faxNumber
            )
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.nameOfSchoolCollegeOrUniversity must equalTo(Some(nameOfSchoolCollegeOrUniversity))
          f.nameOfMainTeacherOrTutor must equalTo(Some(nameOfMainTeacherOrTutor))
          f.address.get.lineOne must equalTo(Some(addressLineOne))
          f.address.get.lineTwo must equalTo(Some("lineTwo"))
          f.address.get.lineThree must equalTo(Some("lineThree"))
          f.postcode must equalTo(Some(postcode))
          f.phoneNumber must equalTo(Some(phoneNumber))
          f.faxNumber must equalTo(Some(faxNumber))
        }
      )
    }
    
    "allow optional fields to be left blank" in {
      G2AddressOfSchoolCollegeOrUniversity.form.bind(
        Map("address.street.lineOne" -> "")
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.nameOfSchoolCollegeOrUniversity must equalTo(None)
          f.nameOfMainTeacherOrTutor must equalTo(None)
          f.address must equalTo(None)
          f.postcode must equalTo(None)
          f.phoneNumber must equalTo(None)
          f.faxNumber must equalTo(None)
        }
      )
    }
    
    "reject an invalid postcode" in {
      G2AddressOfSchoolCollegeOrUniversity.form.bind(
        Map("postcode" -> "INVALID")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.postcode"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject an invalid phone number" in {
      G2AddressOfSchoolCollegeOrUniversity.form.bind(
        Map("phoneNumber" -> "INVALID")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.invalid"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }
    
    "reject an invalid fax number" in {
      G2AddressOfSchoolCollegeOrUniversity.form.bind(
        Map("phoneNumber" -> "INVALID")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.invalid"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }
  } section("unit", models.domain.Education.id)
}