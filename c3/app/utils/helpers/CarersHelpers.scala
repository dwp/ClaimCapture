package utils.helpers

import views.html.helper.FieldElements
import scala.language.implicitConversions


class EnhancedFieldElements(f:FieldElements) {

  def carersErrors(): Seq[String] = {
    (f.args.get('_error) match {
      case Some(Some(play.api.data.FormError(_, messages, args))) => Some(Seq(play.api.i18n.MMessages(messages, args: _*)(f.lang)))
      case _ => None
    }).getOrElse {
      if (f.args.get('_showErrors) match {
        case Some(false) => false
        case _ => true
      }) {
        f.field.errors.map(e => play.api.i18n.MMessages(e.message, e.args: _*)(f.lang))
      } else Nil
    }
  }
}

object CarersHelpers {
  implicit def enhanceFieldElements(f:FieldElements) = new EnhancedFieldElements(f)
}
