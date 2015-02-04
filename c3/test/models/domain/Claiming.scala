package models.domain

import controllers.Iteration.{Identifier => IterationID}

trait Claiming extends MockForm {
  def mockJobQuestionGroup(id: String): QuestionGroup with IterationID = {
    val questionGroupIdentifier = mock[QuestionGroup.Identifier]
    questionGroupIdentifier.id returns id

    val questionGroup = mock[QuestionGroup with IterationID]
    questionGroup.identifier returns questionGroupIdentifier

    questionGroup
  }
}
