package models.domain

object AssistedDecision extends Identifier(id = "s15")

case class AssistedDecisionDetails(reason: String = "", recommendation: String = "None,show table") extends QuestionGroup(AssistedDecisionDetails)

object AssistedDecisionDetails extends QGIdentifier(id = s"${AssistedDecision.id}.g1")
