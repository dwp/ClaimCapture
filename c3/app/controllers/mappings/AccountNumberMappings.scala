package controllers.mappings

import play.api.data.validation.{ValidationError, _}
import utils.CommonValidation._

object AccountNumberMappings {
  def accountNumberFilledIn: Constraint[String] = Constraint[String]("constraint.restrictedStringText") { restrictedString =>
    restrictedString.replaceAll(" ", "").length match {
      case 0 => Invalid(ValidationError("error.accountnumber.empty"))
      case _ => Valid
    }
  }

  def accountNumberDigits: Constraint[String] = Constraint[String]("constraint.restrictedStringText") { restrictedString =>
    NUMBER_REGEX.r.pattern.matcher(restrictedString.replaceAll(" ", "")).matches match {
      case true => Valid
      case false => Invalid(ValidationError("error.accountnumber.nondigits"))
    }
  }

  def accountNumberLength: Constraint[String] = Constraint[String]("constraint.restrictedStringText") { restrictedString =>
    if( restrictedString.replaceAll(" ", "").length < ACCOUNT_NUMBER_MIN_LENGTH) Invalid(ValidationError("error.accountnumber.tooshort", ACCOUNT_NUMBER_MIN_LENGTH))
    else if(restrictedString.replaceAll(" ", "").length > ACCOUNT_NUMBER_MAX_LENGTH) Invalid(ValidationError("error.accountnumber.toolong", ACCOUNT_NUMBER_MAX_LENGTH))
    else Valid
  }
}
