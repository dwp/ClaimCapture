package controllers.mappings

import models.MultiLineAddress
import play.api.data.Forms._
import play.api.data.Mapping
import play.api.data.validation._

object AddressMappings {

  private val MAX_LINE_LENGTH = 35

  val address: Mapping[MultiLineAddress] = mapping(
    "lineOne" -> optional(text),
    "lineTwo" -> optional(text),
    "lineThree" -> optional(text)
  )(MultiLineAddress.apply)(MultiLineAddress.unapply).verifying(basicValidations)

  def requiredAddress = Constraint[MultiLineAddress]("constraint.required") { a =>
    if(invalidateSpaces(a.lineOne).isEmpty) Invalid(ValidationError("error.required"))
    else if (invalidateSpaces(a.lineTwo).isEmpty) Invalid(ValidationError("error.addressLines.required"))
    else Valid
  }

  private def invalidateSpaces (value:Option[String]) = {
      value.flatMap{
        case s:String if s.trim.isEmpty => None
        case s:String => Some(s)
      }
  }

  private def basicValidations = Constraint[MultiLineAddress]("addres.basic"){ address =>

    val values = Seq(address.lineOne, address.lineTwo, address.lineThree).map(_.getOrElse(""))
    val constraints = Seq(Constraints.maxLength(MAX_LINE_LENGTH), Mappings.restrictedStringText)
    val results = constraints.map(applyConstraintToElems(values,_)).flatten

    if (isAnyInvalid(results)) {
      groupInvalids(results)
    } else {
      Valid
    }

  }


  private def isAnyInvalid(results: Seq[ValidationResult]) = {
    results.exists(_.isInstanceOf[Invalid])
  }
  private def applyConstraintToElems[T](elems:Seq[T],constraint:Constraint[T]) = {
    elems.map(e => constraint.apply(e))
  }

  private def groupInvalids(results: Seq[ValidationResult]) = {
    Invalid(results.filter(_.isInstanceOf[Invalid]).map(e => e.asInstanceOf[Invalid].errors).flatten)
  }

}
