package models.domain

import models.domain.PaymentsFromAbroad.GQuestion
import models.{DayMonthYear}
import models.yesNo.{YesNoWith2MandatoryFieldsOnYes, YesNoWith1MandatoryFieldOnYes, YesNoWithText}
import play.api.data.validation.{ValidationError, Invalid, Valid, Constraint}
import controllers.mappings.Mappings.yes
import utils.helpers.OriginTagHelper._

object NationalityAndResidencySection extends Section.Identifier {
  val id = "s22"
}
case class NationalityAndResidency(nationality: String = "",
                                   actualnationality: Option[String] = None,
                                   alwaysLivedInUK: String = "",
                                   liveInUKNow: Option[String] = None,
                                   arrivedInUK: Option[String] = None,
                                   arrivedInUKDate: Option[DayMonthYear] = None,
                                   arrivedInUKFrom: Option[String] = None,
                                   trip52weeks: String = "",
                                   tripDetails: Option[String] = None
                                    ) extends QuestionGroup(NationalityAndResidency)

object NationalityAndResidency extends QuestionGroup.Identifier {
  val id = s"${NationalityAndResidencySection.id}.g1"

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

  def resideInUK(nationalityAndResidency: NationalityAndResidency): Boolean = {
    (nationalityAndResidency.alwaysLivedInUK, nationalityAndResidency.liveInUKNow, nationalityAndResidency.arrivedInUK) match {
      case ("yes", _, _) => true
      case (_, Some("yes"), Some("more")) => true
      case (_, _, _) => false
    }
  }
}

case class PaymentsFromAbroad(guardQuestion: GQuestion = YesNoWith2MandatoryFieldsOnYes()) extends QuestionGroup(PaymentsFromAbroad)

object PaymentsFromAbroad extends QuestionGroup.Identifier {
  val id = s"${NationalityAndResidencySection.id}.g2"

  type GQuestion = YesNoWith2MandatoryFieldsOnYes[YesNoWith1MandatoryFieldOnYes[String], YesNoWith1MandatoryFieldOnYes[String]]

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
}

