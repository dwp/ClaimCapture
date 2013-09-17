package models.domain

import play.api.i18n.Messages

abstract class QuestionGroup(val identifier: QuestionGroup.Identifier) {
  val definition: String = Messages(identifier.id)
  def numberOfCharactersInput: Int = 0
}

object QuestionGroup {
  trait Identifier {
    val id: String

    lazy val index: Int = id.dropWhile(!_.equals('g')).drop(1).toInt

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