package models.domain

import app.XMLValues._
import models.{DayMonthYear, NationalInsuranceNumber}


object CircumstancesIdentification extends Identifier(id = "c1")

case class ReportChangeOrigin(origin: String = NotAsked) extends QuestionGroup(ReportChangeOrigin)
object ReportChangeOrigin extends QGIdentifier(id = s"${CircumstancesIdentification.id}.g1")


case class ReportChangeReason(jsEnabled: Boolean = false, reportChanges: String = NotAsked) extends QuestionGroup(ReportChangeReason)

object ReportChangeReason extends QGIdentifier(id = s"${CircumstancesIdentification.id}.g1")

case class CircumstancesYourDetails( firstName: String = "",
                                     surname: String = "",
                                     nationalInsuranceNumber: NationalInsuranceNumber = NationalInsuranceNumber(Some("")),
                                     dateOfBirth: DayMonthYear = DayMonthYear(None, None, None),
                                     override val wantsContactEmail:String = "",
                                     override val email:Option[String] = None,
                                     override val emailConfirmation:Option[String] = None,
                                     theirFirstName: String = "",
                                     theirSurname: String = "",
                                     theirRelationshipToYou: String = "",
                                     furtherInfoContact: Option[String] = None
                                      ) extends QuestionGroup(CircumstancesYourDetails) with EMail{
}

object CircumstancesYourDetails extends QGIdentifier(id = s"${CircumstancesIdentification.id}.g2")



