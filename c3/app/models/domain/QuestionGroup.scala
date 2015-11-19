package models.domain

import play.api.i18n.{MMessages, MessagesApi, Lang}
import play.api.Play.current

abstract class QuestionGroup(val identifier: QuestionGroup.Identifier) extends Serializable {
  def messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  val definition: String = messagesApi(identifier.id)
}

object QuestionGroup extends Serializable {
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
