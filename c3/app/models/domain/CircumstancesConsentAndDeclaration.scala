package models.domain

import controllers.Mappings._
import scala.Some

case object CircumstancesConsentAndDeclaration extends Section.Identifier {
  val id = "c3"
  //override val expectedMinTimeToCompleteInMillis: Long = 10000
}

case class CircumstancesDeclaration(obtainInfoAgreement: String = "",obtainInfoWhy: Option[String] = Some(""), confirm: String = "", circsSomeOneElse: Option[String] = None, nameOrOrganisation:String = "") extends QuestionGroup(CircumstancesDeclaration)

object CircumstancesDeclaration extends QuestionGroup.Identifier {
  val id = s"${CircumstancesConsentAndDeclaration.id}.g1"

  def validateWhy(input: CircumstancesDeclaration): Boolean = input.obtainInfoAgreement match {
    case `no` => input.obtainInfoWhy.isDefined
    case `yes` => true
  }

  def validateNameOrOrganisation(circumstancesDeclaration: CircumstancesDeclaration) = {
    circumstancesDeclaration.circsSomeOneElse match {
      case Some(s) => !circumstancesDeclaration.nameOrOrganisation.isEmpty
      case _ => true
    }
  }
}