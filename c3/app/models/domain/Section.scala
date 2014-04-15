package models.domain

import play.api.i18n.{MMessages => Messages}

case class Section(identifier: Section.Identifier, questionGroups: List[QuestionGroup] = Nil, visible: Boolean = true) extends Serializable {
  def name = Messages(identifier.id + ".name")

  def questionGroup(questionGroupIdentifier: QuestionGroup.Identifier): Option[QuestionGroup] = {
    questionGroups.find(qg => qg.identifier == questionGroupIdentifier)
  }

  def update(questionGroup: QuestionGroup): Section = {
    val updatedQuestionGroups = questionGroups.takeWhile(_.identifier.index < questionGroup.identifier.index) :::
                                List(questionGroup) :::
                                questionGroups.dropWhile(_.identifier.index <= questionGroup.identifier.index)

    copy(questionGroups = updatedQuestionGroups.sortWith(_.identifier.index < _.identifier.index))
  }

  def +(questionGroup: QuestionGroup): Section = update(questionGroup)

  def delete(questionGroup: QuestionGroup): Section = delete(questionGroup.identifier)

  def -(questionGroup: QuestionGroup): Section = delete(questionGroup)

  def delete(questionGroupIdentifier: QuestionGroup.Identifier): Section = {
    copy(questionGroups = questionGroups.filterNot(qg => qg.identifier == questionGroupIdentifier))
  }

  def -(questionGroupIdentifier: QuestionGroup.Identifier): Section = delete(questionGroupIdentifier)

  def precedingQuestionGroups(questionGroup: QuestionGroup): List[QuestionGroup] = precedingQuestionGroups(questionGroup.identifier)

  def precedingQuestionGroups(questionGroupIdentifier: QuestionGroup.Identifier): List[QuestionGroup] = {
    questionGroups.filter(_.identifier.index < questionGroupIdentifier.index)
  }

  def show = copy(visible = true)

  def hide = copy(visible = false)
}

case object Section extends Serializable {
  def sectionIdentifier(questionGroup: QuestionGroup): Section.Identifier = sectionIdentifier(questionGroup.identifier)

  def sectionIdentifier(questionGroupIdentifier: QuestionGroup.Identifier): Section.Identifier = {
    new Section.Identifier {
      override val id: String = questionGroupIdentifier.id.split('.')(0)
    }
  }

  trait Identifier extends Serializable {
    val id: String
    
    def index = id.drop(1).toInt

    def name(implicit claim: Claim) = claim.section(this).name

    def visible(implicit claim: Claim) = claim.section(this).visible

    override def equals(other: Any) = other match {
      case that: Identifier => id == that.id
      case _ => false
    }

    override def hashCode() = {
      val prime = 41
      prime + id.hashCode
    }
  }
}