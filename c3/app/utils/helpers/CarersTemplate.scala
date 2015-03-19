package utils.helpers

import views.html.helper.FieldConstructor

object CarersTemplate {
  implicit val fieldConstructor = FieldConstructor(views.html.helper.templates.carersTemplate.f)
}

object CheckTemplate {
  implicit val fieldConstructor = FieldConstructor(views.html.helper.templates.checkTemplate.f)
}

object EmptyTemplate {
  implicit val fieldConstructor = FieldConstructor(views.html.helper.templates.emptyTemplate.f)
}

//object DateTimeTemplate {
//  implicit val fieldConstructor = FieldConstructor(views.html.helper.templates.dateTimeTemplate.f)
//}

object ShortFieldTemplate {
  implicit val fieldConstructor = FieldConstructor(views.html.helper.templates.shortFieldTemplate.f)
}

object CurrencyTemplate {
  implicit val fieldConstructor = FieldConstructor(views.html.helper.templates.shortFieldCurrencyTemplate.f)
}

object DatepickerTemplate {
  implicit val fieldConstructor = FieldConstructor(views.html.helper.templates.datepickerTemplate.f)
}

object EligibilityTemplate {
  implicit val fieldConstructor = FieldConstructor(views.html.helper.templates.eligibilityTemplate.f)
}
