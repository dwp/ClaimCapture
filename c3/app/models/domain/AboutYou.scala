package models.domain

import app.ConfigProperties._
import gov.dwp.carers.xml.validation.CommonValidation._
import models.{NationalInsuranceNumber, MultiLineAddress, DayMonthYear}
import play.api.data.validation.{ValidationError, Invalid, Valid, Constraint}

object AboutYou extends Section.Identifier {
  val id = "s3"
}

case class YourDetails(title: String = "",
                       firstName: String = "",
                       middleName: Option[String] = None,
                       surname: String = "",
                       nationalInsuranceNumber: NationalInsuranceNumber = NationalInsuranceNumber(None),
                       dateOfBirth: DayMonthYear = DayMonthYear(None, None, None)) extends QuestionGroup(YourDetails) {

  def otherNames = firstName + middleName.map(" " + _).getOrElse("")
}

object YourDetails extends QuestionGroup.Identifier {
  val id = s"${AboutYou.id}.g1"

  def getSwitchedNameRegex() = getBooleanProperty("surname-drs-regex") match {
    case (true) => NAME_REGEX
    case (_) => RESTRICTED_CHARS
  }

  def getNameBadCharacters(text: String): String = {
    val sb = new StringBuffer
    text.map(c => {
      if (!NAME_CHARS.r.pattern.matcher(c.toString).matches()) {
        if (sb.length() > 0) sb.append(",")
        sb.append("\"" + c + "\"")
      }
    })
    sb.toString
  }

  def validName: Constraint[String] = Constraint[String]("constraint.restrictedStringText") { text =>
    ( getBooleanProperty("surname-drs-regex") , getSwitchedNameRegex().r.pattern.matcher(text).matches ) match {
      case(_, true) => Valid
      case(false,false) => Invalid(ValidationError("error.restricted.characters"))
      case(true,false) => {
        val badchars=getNameBadCharacters(text)
        if(!badchars.equals("")){
          Invalid(ValidationError("error.name.restricted.characters", badchars))
        }
        else if(! "^[a-zA-Z].*".r.pattern.matcher(text).matches){
          Invalid(ValidationError("error.name.badstart.character", badchars))
        }
        else if(! ".*[a-zA-Z]$".r.pattern.matcher(text).matches){
          Invalid(ValidationError("error.name.badend.character", badchars))
        }
        else{
          Invalid(ValidationError("Unknown error validating name against regex"))
        }
      }
    }
  }
}

case class MaritalStatus(maritalStatus: String = "") extends QuestionGroup(MaritalStatus)

object MaritalStatus extends QuestionGroup.Identifier {
  val id = s"${AboutYou.id}.g2"
}

case class ContactDetails(address: MultiLineAddress = new MultiLineAddress(),
                          postcode: Option[String] = None,
                          howWeContactYou: Option[String] = None,
                          contactYouByTextphone: Option[String] = None,
                          override val wantsContactEmail: String = "",
                          override val email: Option[String] = None,
                          override val emailConfirmation: Option[String] = None) extends QuestionGroup(ContactDetails) with EMail

object ContactDetails extends QuestionGroup.Identifier {
  val id = s"${AboutYou.id}.g3"
}

