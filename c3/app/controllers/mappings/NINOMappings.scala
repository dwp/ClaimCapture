package controllers.mappings

import play.api.data.Mapping
import play.api.data.Forms._
import play.api.data.validation._
import models.NationalInsuranceNumber
import play.api.data.validation.ValidationError


object NINOMappings {

  def nino: Mapping[NationalInsuranceNumber] = mapping(
    "nino" -> optional(text)
  )(NationalInsuranceNumber.apply)(NationalInsuranceNumber.unapply)

  private def ninoValidation(nino: NationalInsuranceNumber): ValidationResult = {
    val ninoPattern = """(([A-CEHJ-MOPRSW-Y]{1}[A-CEGHJ-NPR-TW-Z]{1}[0-9]{6})|([G]{1}[ACEGHJ-NPR-TW-Z]{1}[0-9]{6})|([N]{1}[A-CEGHJL-NPR-TW-Z]{1}[0-9]{6})|([T]{1}[A-CEGHJ-MPR-TW-Z]{1}[0-9]{6})|([Z]{1}[A-CEGHJ-NPR-TW-Y]{1}[0-9]{6}))(([A-D ]{1})|([\S\s\d]{0,0}))""".r

    ninoPattern.pattern.matcher(nino.nino.get.toUpperCase).matches match {
      case true => Valid
      case false => Invalid(ValidationError("error.nationalInsuranceNumber"))
    }
  }

  def filledInNino: Constraint[NationalInsuranceNumber] = Constraint[NationalInsuranceNumber]("constraint.required") {
    case NationalInsuranceNumber(Some(_)) => Valid
    case _ => Invalid(ValidationError(Mappings.errorRequired))
  }

  def validNino: Constraint[NationalInsuranceNumber] = Constraint[NationalInsuranceNumber]("constraint.nino") {
    case nino@NationalInsuranceNumber(Some(_)) => ninoValidation(nino)
    case _ => Invalid(ValidationError("error.nationalInsuranceNumber"))
  }

}
