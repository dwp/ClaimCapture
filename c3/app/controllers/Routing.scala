package controllers

import models.domain.{Claim, QuestionGroup}
import play.api.mvc.Call

@deprecated(message = "Will be deleted once refactored all code to new navigation/tracking", since = "15 Aug 2013")
trait Routing {
  def route(questionGroupIdentifier: QuestionGroup.Identifier): Call

  def route(questionGroup: QuestionGroup): Call = route(questionGroup.identifier)

  def completedQuestionGroups(questionGroupIdentifier: QuestionGroup.Identifier)(implicit claim: Claim): List[(QuestionGroup, Call)] = {
    claim.completedQuestionGroups(questionGroupIdentifier).map(qg => qg -> route(qg))
  }
}