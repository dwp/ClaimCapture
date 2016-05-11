package utils.helpers

import views.html.helper.FieldElements
import scala.language.implicitConversions
import play.api.i18n.{Lang, MMessages, MessagesApi}
import play.api.Play.current

class EnhancedFieldElements(f:FieldElements)(implicit lang: Lang) {
  val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  def carersErrors(): Seq[String] = {
    (f.args.get('_error) match {
      case Some(Some(play.api.data.FormError(_, messages, args))) => Some(Seq(messagesApi(messages, args: _*)))
      case _ => None
    }).getOrElse {
      if (f.args.get('_showErrors) match {
        case Some(false) => false
        case _ => true
      }) {
        f.field.errors.map(e => messagesApi(e.message, e.args: _*))
      } else Nil
    }
  }
}

object CarersHelpers {
  implicit def enhanceFieldElements(f:FieldElements)(implicit lang: Lang):EnhancedFieldElements = new EnhancedFieldElements(f)
}
