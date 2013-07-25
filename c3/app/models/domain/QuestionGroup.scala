package models.domain

import models.view.Routing
import play.api.mvc.Call
import play.api.i18n.Messages

abstract class QuestionGroup(val identifier: QuestionGroup.Identifier) extends Routing {
  val definition: String = Messages(identifier.id)
}

object QuestionGroup {
  trait Identifier {
    val id: String

    lazy val index: Int = id.dropWhile(!_.equals('g')).drop(1).toInt

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

trait NoRouting extends Routing {
  this: QuestionGroup =>

  override val call: Call = Call("", "")
}

object NoRouting {
  import language.implicitConversions

  implicit def noRouting(nr: NoRouting.type) = Call("", "")
}