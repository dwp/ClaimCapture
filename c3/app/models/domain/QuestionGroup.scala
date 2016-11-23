package models.domain

import play.api.i18n.{MMessages, MessagesApi, Lang}
import play.api.Play.current

abstract class QuestionGroup(val identifier: QGIdentifier) extends Serializable {
  def messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  val definition: String = messagesApi(identifier.id)
}

case class QGIdentifier(id: String) extends Serializable {
  def index: Int = id.dropWhile(!_.equals('g')).drop(1).toInt

  override def equals(other: Any) = other match {
    case that: QGIdentifier => id == that.id
    case _ => false
  }

  override def hashCode() = {
    val prime = 41
    prime + id.hashCode
  }
}
