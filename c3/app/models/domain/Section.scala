package models.domain

import play.api.mvc.Call

case class Section(identifier: Section.Identifier, questionGroups: List[QuestionGroup] = Nil, visible: Boolean = true, firstPage: Call = Call("",""), lastPage: Call = Call("","")) {
  def questionGroup(questionGroupIdentifier: QuestionGroup.Identifier): Option[QuestionGroup] = {
    questionGroups.find(qg => qg.identifier == questionGroupIdentifier)
  }

  def update(questionGroup: QuestionGroup): Section = {
    val updatedQuestionGroups = questionGroups.takeWhile(_.identifier.index < questionGroup.identifier.index) :::
                                List(questionGroup) :::
                                questionGroups.dropWhile(_.identifier.index <= questionGroup.identifier.index)

    copy(questionGroups = updatedQuestionGroups)
  }

  def delete(questionGroup: QuestionGroup): Section = delete(questionGroup.identifier)

  def delete(questionGroupIdentifier: QuestionGroup.Identifier): Section = {
    copy(questionGroups = questionGroups.filterNot(qg => qg.identifier == questionGroupIdentifier))
  }

  def precedingQuestionGroups(questionGroup: QuestionGroup): List[QuestionGroup] = precedingQuestionGroups(questionGroup.identifier)

  def precedingQuestionGroups(questionGroupIdentifier: QuestionGroup.Identifier): List[QuestionGroup] = {
    questionGroups.takeWhile(_.identifier.index < questionGroupIdentifier.index)
  }

  def show(): Section = copy(visible = true)

  def hide(): Section = copy(visible = false)
}

case object Section {
  def sectionIdentifier(questionGroup: QuestionGroup): Section.Identifier = sectionIdentifier(questionGroup.identifier)

  def sectionIdentifier(questionGroupIdentifier: QuestionGroup.Identifier): Section.Identifier = {
    new Section.Identifier { override val id: String = questionGroupIdentifier.id.split('.')(0) }
  }

  def index(sectionIdentifier: Section.Identifier): Int = sectionIdentifier.id.drop(1).toInt

  trait Identifier {
    val id: String

    override def equals(other: Any) = {
      other match {
        case that: Identifier => id == that.id
        case _ => false
      }
    }

    override def hashCode() = {
      val prime = 41
      prime + id.hashCode
    }
  }
}