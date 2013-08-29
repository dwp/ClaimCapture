package controllers

import play.api.mvc.Call
import models.domain.{Claim, QuestionGroup}

trait Routing {
  def route(questionGroupIdentifier: QuestionGroup.Identifier): Call

  def route(questionGroup: QuestionGroup): Call = route(questionGroup.identifier)

  def completedQuestionGroups(questionGroupIdentifier: QuestionGroup.Identifier)(implicit claim: Claim): List[(QuestionGroup, Call)] = {
    claim.completedQuestionGroups(questionGroupIdentifier).map(qg => qg -> route(qg))
  }
}