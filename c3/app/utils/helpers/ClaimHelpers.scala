package utils.helpers

import views.html.helper.FieldConstructor

object ClaimHelpers {
  implicit val myFields = FieldConstructor(views.html.helper.carersText.f)
}

object EmptyHelper {
  implicit val myFields = FieldConstructor(views.html.common.emptyLayout.f)
}