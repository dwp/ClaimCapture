package controllers

import play.api.templates.Html
import play.api.i18n.{MMessages => Messages}
import scala.concurrent.ExecutionContext
import play.api.i18n.Lang

class PreviewUtils {

}

object PreviewUtils {

  var routesMap = PreviewRouteUtils.yourDetailsRoute ++ PreviewRouteUtils.otherMoneyRoute ++ PreviewRouteUtils.educationRoute

  def fieldWithLink(id:String, name:String, value:String)(implicit lang:Lang) = {
    val label = Html(Messages(name))
    val row = s"<dt><a id=$id href=${routesMap(id)}>$label</a></dt><dd>$value</dd>"
    Html(row)
  }

  def apply() = new PreviewUtils()

}


