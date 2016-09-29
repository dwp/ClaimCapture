package models.domain

object AssistedDecision extends Section.Identifier {
  val id = "s15"
}

case class AssistedDecisionDetails(reason: String = "", recommendation: String = "None,show table") extends QuestionGroup(AssistedDecisionDetails)

object AssistedDecisionDetails extends QuestionGroup.Identifier {
  val id = s"${AssistedDecision.id}.g1"
}