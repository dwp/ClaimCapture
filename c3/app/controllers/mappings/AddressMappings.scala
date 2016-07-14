package controllers.mappings

import models.MultiLineAddress
import play.api.data.Forms._
import play.api.data.Mapping
import play.api.data.validation._
import utils.CommonValidation._

object AddressMappings {

  private val MAX_LINE_LENGTH = 35
  val errorRestrictedAddressCharacters = "error.address.invalid.characters"

  val address: Mapping[MultiLineAddress] = mapping(
    "lineOne" -> optional(text),
    "lineTwo" -> optional(text),
    "lineThree" -> optional(text)
  )(MultiLineAddress.apply)(MultiLineAddress.unapply).verifying(basicValidations)

  private def invalidateSpace : Constraint[String] = Constraint[String]("constraint.requiredAddress") { restrictedString =>
    restrictedString match {
      case s if s.trim.isEmpty => Invalid(ValidationError("error.address.lines.required"))
      case _ => Valid
    }
  }

  private def basicValidations = Constraint[MultiLineAddress]("address.basic"){ address =>
    val results = addressValidations(address) ++ lineThreeValidations(address.lineThree)

    if (isAnyInvalid(results)) {
      groupInvalids(results)
    } else {
      Valid
    }
  }

  private def addressValidations(address: MultiLineAddress) = {
    val values = Seq(address.lineOne, address.lineTwo).map(_.getOrElse(""))
    val constraints = Seq(Constraints.maxLength(MAX_LINE_LENGTH), restrictedAddressStringText, invalidateSpace)
    constraints.map(applyConstraintToElems(values,_)).flatten
  }

  private def lineThreeValidations(lineThree: Option[String]) = {
    val values = Seq(lineThree).map(_.getOrElse(""))
    val constraints = Seq(Constraints.maxLength(MAX_LINE_LENGTH), restrictedAddressStringText)
    constraints.map(applyConstraintToElems(values,_)).flatten
  }

  private def isAnyInvalid(results: Seq[ValidationResult]) = {
    results.exists(_.isInstanceOf[Invalid])
  }
  private def applyConstraintToElems[T](elems:Seq[T],constraint:Constraint[T]) = {
    elems.map(e => constraint.apply(e))
  }

  private def groupInvalids(results: Seq[ValidationResult]) = {
    Invalid(mergeErrorCodes(results.filter(_.isInstanceOf[Invalid]).map(e => e.asInstanceOf[Invalid].errors).flatten.distinct))
  }

  private def restrictedAddressStringText: Constraint[String] = Constraint[String]("constraint.restrictedAddressStringText") { restrictedString =>
    val restrictedStringPattern = RESTRICTED_CHARS.r
    restrictedStringPattern.pattern.matcher(restrictedString).matches match {
      case true => Valid
      case false => Invalid(ValidationError(errorRestrictedAddressCharacters))
    }
  }

  private def mergeErrorCodes(results: Seq[ValidationError]) = {
    //merge (constraint.restrictedAddressStringText and error.addressLines.required) into "error.address.lines.required.invalid.characters"
    if (results.length >= 2) {
      Seq(ValidationError("error.address.lines.required.invalid.characters"))
    }
    else results
  }

}
