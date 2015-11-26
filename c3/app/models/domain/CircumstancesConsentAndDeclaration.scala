package models.domain

import controllers.mappings.Mappings._

case object CircumstancesConsentAndDeclaration extends Section.Identifier {
  val id = "c3"
}

case class CircumstancesDeclaration(jsEnabled: Boolean = false,
                                    obtainInfoAgreement: String = "",
                                    obtainInfoWhy: Option[String] = Some(""),
                                    circsSomeOneElse: Option[String] = None,
                                    nameOrOrganisation: Option[String] = None
                                   ) extends QuestionGroup(CircumstancesDeclaration)

object CircumstancesDeclaration extends QuestionGroup.Identifier {
  val id = s"${CircumstancesConsentAndDeclaration.id}.g1"

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
