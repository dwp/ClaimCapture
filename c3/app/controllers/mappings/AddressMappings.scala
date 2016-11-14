package controllers.mappings

import models.MultiLineAddress
import play.api.data.Forms._
import play.api.data.Mapping
import play.api.data.validation._
import utils.CommonValidation._

object AddressMappings {
  val CARER = "careraddress"
  val CARERPREVIOUS = "carerprevaddress"
  val CARERNEW = "carernewaddress"
  val CAREE = "careeaddress"
  val CAREENEW = "careenewaddress"
  val EMPLOYMENT = "empaddress"

  private val MAX_LINE_LENGTH = 35

  def address(atype: String): Mapping[MultiLineAddress] = {
    mapping(
      "lineOne" -> optional(text),
      "lineTwo" -> optional(text),
      "lineThree" -> optional(text)
    )(MultiLineAddress.apply)(MultiLineAddress.unapply).verifying(basicValidations(atype))
  }

  private def errorForAddressType(addressType: String, errorStub: String) = {
    val addressKey = addressType match {
      case CARER => CARER
      case _ => "ADDRESS_TYPE_ERROR"
    }
    s"error.$addressType.$errorStub"
  }

  private def invalidateSpace(addressType: String): Constraint[String] = Constraint[String]("constraint.requiredAddress") { restrictedString =>
    restrictedString match {
      case s if s.trim.isEmpty => Invalid(ValidationError(errorForAddressType(addressType, "lines.required")))
      case _ => Valid
    }
  }

  private def basicValidations(addressType: String) = Constraint[MultiLineAddress]("address.basic"){ address =>
    val results = addressValidations(addressType, address) ++ lineThreeValidations(addressType, address.lineThree)
    if (isAnyInvalid(results)) {
      groupInvalids(addressType, results)
    } else {
      Valid
    }
  }

  private def addressValidations(addressType: String, address: MultiLineAddress) = {
    val values = Seq(address.lineOne, address.lineTwo).map(_.getOrElse(""))
    val constraints = Seq(Constraints.maxLength(MAX_LINE_LENGTH), restrictedAddressStringText(addressType), invalidateSpace(addressType))
    constraints.map(applyConstraintToElems(values, _)).flatten
  }

  private def lineThreeValidations(addressType: String, lineThree: Option[String]) = {
    val values = Seq(lineThree).map(_.getOrElse(""))
    val constraints = Seq(Constraints.maxLength(MAX_LINE_LENGTH), restrictedAddressStringText(addressType))
    constraints.map(applyConstraintToElems(values, _)).flatten
  }

  private def isAnyInvalid(results: Seq[ValidationResult]) = {
    results.exists(_.isInstanceOf[Invalid])
  }

  private def applyConstraintToElems[T](elems: Seq[T], constraint: Constraint[T]) = {
    elems.map(e => constraint.apply(e))
  }

  private def groupInvalids(addressType: String, results: Seq[ValidationResult]) = {
    Invalid(mergeErrorCodes(addressType, results.filter(_.isInstanceOf[Invalid]).map(e => e.asInstanceOf[Invalid].errors).flatten.distinct))
  }

  private def restrictedAddressStringText(addressType: String): Constraint[String] = Constraint[String]("constraint.restrictedAddressStringText") { restrictedString =>
    val restrictedStringPattern = RESTRICTED_CHARS.r
    restrictedStringPattern.pattern.matcher(restrictedString).matches match {
      case true => Valid
      case false => Invalid(ValidationError(errorForAddressType(addressType, "invalid.characters")))
    }
  }

  private def mergeErrorCodes(addressType: String, results: Seq[ValidationError]) = {
    //merge (constraint.restrictedAddressStringText and error.addressLines.required) into "error.address.lines.required.invalid.characters"
    if (results.length >= 2) {
      Seq(ValidationError(errorForAddressType(addressType, "lines.required.invalid.characters")))
    }
    else results
  }

}
