package models.domain

import controllers.mappings.Mappings._

object CircumstancesConsentAndDeclaration extends Identifier(id = "c3")

case class CircumstancesDeclaration(jsEnabled: Boolean = false,
                                    obtainInfoAgreement: String = "",
                                    obtainInfoWhy: Option[String] = Some(""),
                                    circsSomeOneElse: Option[String] = None,
                                    nameOrOrganisation: Option[String] = None
                                   ) extends QuestionGroup(CircumstancesDeclaration)

object CircumstancesDeclaration extends QGIdentifier(id = s"${CircumstancesConsentAndDeclaration.id}.g1") {

  def validateWhy(input: CircumstancesDeclaration): Boolean = input.obtainInfoAgreement match {
    case `no` => input.obtainInfoWhy.isDefined
    case `yes` => true
  }

  def validateNameOrOrganisation(circumstancesDeclaration: CircumstancesDeclaration) = {
    circumstancesDeclaration.circsSomeOneElse match {
      case Some(s) => circumstancesDeclaration.nameOrOrganisation.nonEmpty
      case _ => true
    }
  }
}
