package models.domain

trait Claiming extends MockForm {
  def mockJobQuestionGroup(id: String): QuestionGroup with Job.Identifier = {
    val questionGroupIdentifier = mock[QuestionGroup.Identifier]
    questionGroupIdentifier.id returns id

    val questionGroup = mock[QuestionGroup with Job.Identifier]
    questionGroup.identifier returns questionGroupIdentifier

    questionGroup
  }
}
