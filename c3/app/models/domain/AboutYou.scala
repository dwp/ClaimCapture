package models.domain

import models.domain.OtherEEAStateOrSwitzerland.GQuestion
import models.{NationalInsuranceNumber, MultiLineAddress, DayMonthYear}
import models.yesNo.{YesNo, YesNoWith2MandatoryFieldsOnYes, YesNoWith1MandatoryFieldOnYes, YesNoWithText}
import play.api.data.validation.{ValidationError, Invalid, Valid, Constraint}
import controllers.mappings.Mappings.yes
import utils.helpers.OriginTagHelper._

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

case class NationalityAndResidency( nationality: String,
                                    actualnationality: Option[String] = None,
                                    alwaysLivedInUK: String = "",
                                    liveInUKNow: Option[String] = None,
                                    arrivedInUK: Option[String] = None,
                                    arrivedInUKDate: Option[DayMonthYear] = None,
                                    trip52weeks: String = "",
                                    tripDetails: Option[String] = None
                                    ) extends QuestionGroup(NationalityAndResidency)

object NationalityAndResidency extends QuestionGroup.Identifier {
  val id = s"${AboutYou.id}.g4"

  val british = "British"
  val britishIrish = "British/Irish"
  val anothercountry = "Another nationality"
  val lessThan3Years = "less"
  val moreThan3Years = "more"

  def validNationality: Constraint[String] = Constraint[String]("constraint.nationality") {
    case `british` if (isOriginGB) => Valid
    case `britishIrish` if (!isOriginGB) => Valid
    case `anothercountry` => Valid
    case _ => Invalid(ValidationError("nationality.invalid"))
  }

  def actualNationalityRequired: Constraint[NationalityAndResidency] = Constraint[NationalityAndResidency]("constraint.actualnationality") { nationalityAndResidency =>
    if (nationalityAndResidency.nationality == anothercountry) {
      nationalityAndResidency.actualnationality match {
        case Some(place) => Valid
        case _ => Invalid(ValidationError("actualnationality.required"))
      }
    }
    else Valid
  }

  def requiredTripDetails: Constraint[NationalityAndResidency] = Constraint[NationalityAndResidency]("constraint.tripdetails") { nationalityAndResidency =>
    nationalityAndResidency.trip52weeks match {
      case `yes` if !nationalityAndResidency.tripDetails.isDefined => Invalid(ValidationError("tripdetails.required"))
      case _ => Valid
    }
  }

  def requiredLiveInEnglandNow: Constraint[NationalityAndResidency] = Constraint[NationalityAndResidency]("constraint.tripdetails") { nationalityAndResidency =>
    nationalityAndResidency.trip52weeks match {
      case `yes` if !nationalityAndResidency.tripDetails.isDefined => Invalid(ValidationError("tripdetails.required"))
      case _ => Valid
    }
  }
}

case class AbroadForMoreThan52Weeks(anyTrips: String = "", tripDetails: Option[String] = None) extends QuestionGroup(AbroadForMoreThan52Weeks)

/*
REDUNDANT DELETE
 */
object AbroadForMoreThan52Weeks extends QuestionGroup.Identifier {
  val id = s"${AboutYou.id}.g5"

  def requiredTripDetails: Constraint[AbroadForMoreThan52Weeks] = Constraint[AbroadForMoreThan52Weeks]("constraint.tripdetails") { abroadForMoreThan52Weeks =>
    abroadForMoreThan52Weeks.anyTrips match {
      case `yes` if !abroadForMoreThan52Weeks.tripDetails.isDefined => Invalid(ValidationError("tripdetails.required"))
      case _ => Valid
    }
  }

}

case class OtherEEAStateOrSwitzerland(guardQuestion:GQuestion = YesNoWith2MandatoryFieldsOnYes()) extends QuestionGroup(OtherEEAStateOrSwitzerland)

object OtherEEAStateOrSwitzerland extends QuestionGroup.Identifier {
  type GQuestion = YesNoWith2MandatoryFieldsOnYes[YesNoWith1MandatoryFieldOnYes[String],YesNoWith1MandatoryFieldOnYes[String]]

  def requiredBenefitsFromEEADetails: Constraint[GQuestion] = Constraint[GQuestion]("constraint.benefitsfromeeadetails") { qg =>
    qg.answer match {
      case `yes` =>
        qg.field1 match {
          case Some(YesNoWith1MandatoryFieldOnYes(`yes`,None)) => Invalid(ValidationError("benefitsfromeeadetails.required"))
          case _ => Valid
        }

      case _ => Valid
    }
  }

  def requiredWorkingForEEADetails: Constraint[GQuestion] = Constraint[GQuestion]("constraint.workingForEEADetails") { qg =>
    qg.answer match {
      case `yes` =>
        qg.field2 match {
          case Some(YesNoWith1MandatoryFieldOnYes(`yes`,None)) => Invalid(ValidationError("workingForEEADetails.required"))
          case _ => Valid
        }
      case _ => Valid
    }
  }

  val id = s"${AboutYou.id}.g7"
}

