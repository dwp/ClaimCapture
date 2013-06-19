package utils.helpers

import views.html.helper.FieldConstructor

object ClaimHelpers {
  implicit val myFields = FieldConstructor(views.html.helper.carersText.f)
}