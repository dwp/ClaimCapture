package models.domain

import play.api.i18n.{MMessages, MessagesApi}
import play.api.Play.current

case class Section(identifier: Identifier, questionGroups: List[QuestionGroup] = Nil, visible: Boolean = true) extends Serializable {
  def messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  def name = messagesApi(identifier.id + ".name")

  def questionGroup(questionGroupIdentifier: QGIdentifier): Option[QuestionGroup] = {
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

  def delete(questionGroupIdentifier: QGIdentifier): Section = {
    copy(questionGroups = questionGroups.filterNot(qg => qg.identifier == questionGroupIdentifier))
  }

  def -(questionGroupIdentifier: QGIdentifier): Section = delete(questionGroupIdentifier)

  def precedingQuestionGroups(questionGroup: QuestionGroup): List[QuestionGroup] = precedingQuestionGroups(questionGroup.identifier)

  def precedingQuestionGroups(questionGroupIdentifier: QGIdentifier): List[QuestionGroup] = {
    questionGroups.filter(_.identifier.index < questionGroupIdentifier.index)
  }

  def show = copy(visible = true)

  def hide = copy(visible = false)
}

case object Section extends Serializable {
  def sectionIdentifier(questionGroup: QuestionGroup): Identifier = sectionIdentifier(questionGroup.identifier)

  def sectionIdentifier(questionGroupIdentifier: QGIdentifier): Identifier = {
    new Identifier(questionGroupIdentifier.id.split('.')(0))
  }
}

case class Identifier(id: String) extends Serializable {
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