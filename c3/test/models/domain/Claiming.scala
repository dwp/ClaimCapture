package models.domain

trait Claiming extends MockForm {
  def mockJobQuestionGroup(id: String): QuestionGroup with Iteration.Identifier = {
    val questionGroupIdentifier = mock[QuestionGroup.Identifier]
    questionGroupIdentifier.id returns id

    val questionGroup = mock[QuestionGroup with Iteration.Identifier]
    questionGroup.identifier returns questionGroupIdentifier

    questionGroup
  }
}
