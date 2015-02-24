package models.domain

import controllers.mappings.Mappings
import models.{NationalInsuranceNumber, MultiLineAddress, DayMonthYear}
import models.yesNo.YesNoWithText
import play.api.data.validation.{ValidationError, Invalid, Valid, Constraint}
import controllers.mappings.Mappings.yes

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
}

case class ContactDetails(address: MultiLineAddress = new MultiLineAddress(),
                          postcode: Option[String] = None,
                          howWeContactYou: String = "",
                          contactYouByTextphone: Option[String] = None,
                          override val wantsContactEmail:Option[String] = None,
                          override val email:Option[String] = None,
                          override val emailConfirmation:Option[String] = None) extends QuestionGroup(ContactDetails) with EMail

object ContactDetails extends QuestionGroup.Identifier {
  val id = s"${AboutYou.id}.g2"
}

case class NationalityAndResidency(nationality: String,
                                   actualnationality: Option[String] = None,
                                   resideInUK: YesNoWithText = YesNoWithText("", None),
                                   maritalStatus: Option[String] = None) extends QuestionGroup(NationalityAndResidency)

object NationalityAndResidency extends QuestionGroup.Identifier {
  val id = s"${AboutYou.id}.g4"

  val british = "British"
  val anothercountry = "Another Country"

  def validNationality: Constraint[String] = Constraint[String]("constraint.nationality") {
    // Nationality is a radio list with two possible values, British and Another Country
    case `british` => Valid
    case `anothercountry` => Valid
    case _ => Invalid(ValidationError("nationality.invalid"))
  }

  def actualNationalityRequired: Constraint[NationalityAndResidency] = Constraint[NationalityAndResidency]("constraint.actualnationality") { nationalityAndResidency =>
    // if the Nationality is Another Country the user must provide their National Residency
    if (nationalityAndResidency.nationality == anothercountry) {
      nationalityAndResidency.actualnationality match {
        case Some(place) => Valid
        case _ => Invalid(ValidationError("actualnationality.required"))
      }
    }
    else Valid
  }

  def maritalStatusRequired: Constraint[NationalityAndResidency] = Constraint[NationalityAndResidency]("constraint.actualnationality") { nationalityAndResidency =>
    // if the Nationality is Another Country the user must provide their National Residency
    if (nationalityAndResidency.nationality == anothercountry) {
      nationalityAndResidency.maritalStatus match {
        case Some(maritalStatus) => Valid
        case _ => Invalid(ValidationError("maritalstatus.required"))
      }
    }
    else Valid
  }

  def validateHadPartner(nationalityAndResidency: NationalityAndResidency) = nationalityAndResidency.maritalStatus.getOrElse("") == "p"
}

case class AbroadForMoreThan52Weeks(anyTrips: String = "", tripDetails:Option[String] = None) extends QuestionGroup(AbroadForMoreThan52Weeks)

object AbroadForMoreThan52Weeks extends QuestionGroup.Identifier  {
  val id = s"${AboutYou.id}.g5"

  def requiredTripDetails: Constraint[AbroadForMoreThan52Weeks] = Constraint[AbroadForMoreThan52Weeks]("constraint.tripdetails") { abroadForMoreThan52Weeks =>
    abroadForMoreThan52Weeks.anyTrips match {
      case `yes` if !abroadForMoreThan52Weeks.tripDetails.isDefined => Invalid(ValidationError("tripdetails.required"))
      case _ => Valid
    }
  }

}

case class OtherEEAStateOrSwitzerland(benefitsFromEEA: String = "", workingForEEA: String = "") extends QuestionGroup(OtherEEAStateOrSwitzerland)

object OtherEEAStateOrSwitzerland extends QuestionGroup.Identifier {
  val id = s"${AboutYou.id}.g7"
}

