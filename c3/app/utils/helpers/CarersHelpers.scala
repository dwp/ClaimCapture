package utils.helpers

import views.html.helper.FieldElements
import scala.language.implicitConversions


class EnhancedFieldElements(f:FieldElements) {

  def carersErrors(implicit lang: play.api.i18n.Lang): Seq[String] = {
    (f.args.get('_error) match {
      case Some(Some(play.api.data.FormError(_, message, args))) => Some(Seq(play.api.i18n.MMessages(message, args: _*)))
      case _ => None
    }).getOrElse {
      if (f.args.get('_showErrors) match {
        case Some(false) => false
        case _ => true
      }) {
        f.field.errors.map(e => play.api.i18n.MMessages(e.message, e.args: _*))
      } else Nil
    }
  }
}

object CarersHelpers {
  implicit def enhanceFieldElements(f:FieldElements) = new EnhancedFieldElements(f)
}
