package models.domain

import play.api.data.validation.{ValidationError, Invalid, Valid, Constraint}

object ThirdParty extends Identifier(id = "s14")

case class ThirdPartyDetails(thirdParty: String = "", nameAndOrganisation: Option[String] = None) extends QuestionGroup(ThirdPartyDetails)

object ThirdPartyDetails extends QGIdentifier(id = s"${ThirdParty.id}.g1") {
  val yesCarer = "yesCarer"
  val noCarer = "noCarer"

  def validThirdParty: Constraint[String] = Constraint[String]("constraint.thirdParty") {
    case `yesCarer` => Valid
    case `noCarer` => Valid
    case _ => Invalid(ValidationError("thirdParty.invalid"))
  }

  def nameAndOrganisationRequired: Constraint[ThirdPartyDetails] = Constraint[ThirdPartyDetails]("constraint.thirdParty.nameAndOrganisation") { thirdPartyDetails =>
    if (thirdPartyDetails.thirdParty == noCarer) {
      thirdPartyDetails.nameAndOrganisation match {
        case Some(org) => Valid
        case _ => Invalid(ValidationError("thirdParty.nameAndOrganisation.required"))
      }
    }
    else Valid
  }
}