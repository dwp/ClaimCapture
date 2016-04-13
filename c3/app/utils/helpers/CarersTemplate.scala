package utils.helpers

import play.api.i18n.Lang
import views.html.helper.FieldConstructor

case class CarersTemplate(lang: Lang) {
  implicit val fieldConstructor = FieldConstructor(views.html.helper.templates.carersTemplate.f(_)(lang))
}

case class CheckTemplate(lang: Lang) {
  implicit val fieldConstructor = FieldConstructor(views.html.helper.templates.checkTemplate.f(_)(lang))
}

case class EmptyTemplate(lang: Lang) {
  implicit val fieldConstructor = FieldConstructor(views.html.helper.templates.emptyTemplate.f(_)(lang))
}

case class ShortFieldTemplate(lang: Lang) {
  implicit val fieldConstructor = FieldConstructor(views.html.helper.templates.shortFieldTemplate.f(_)(lang))
}

case class CurrencyTemplate(lang: Lang) {
  implicit val fieldConstructor = FieldConstructor(views.html.helper.templates.shortFieldCurrencyTemplate.f(_)(lang))
}

case class EligibilityTemplate(lang: Lang) {
  implicit val fieldConstructor = FieldConstructor(views.html.helper.templates.carersTemplate.f(_)(lang))
}

case class CheckboxTemplate(lang: Lang) {
  implicit val fieldConstructor = FieldConstructor(views.html.helper.templates.checkboxTemplate.f(_)(lang))
}

