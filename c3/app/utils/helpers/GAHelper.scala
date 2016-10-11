package utils.helpers

import play.api.Play
import play.api.Play.current

object GAHelper {
  val regexIterationId = "/[0-9a-f]{8}-([0-9a-f]{4}-){3}[0-9a-f]{12}".r

  private def addOpt(s:Option[String]):String = s match{
    case Some(opt) if opt.nonEmpty => ",\""+opt+"\""
    case _ => ""
  }

  def trackEvent(category:String, action:String, label:Option[String]=None, value:Option[String]=None):String = {
    if (!Play.isTest) {
      val newCategory = regexIterationId.replaceFirstIn(category, "");
      s"""trackEvent('$newCategory',\"$action\"${addOpt(label)}${addOpt(value)});""".toString
    } else {
      ""
    }
  }

  def trackPageView(category:String):String = {
    if (!Play.isTest) {
      val newCategory = regexIterationId.replaceFirstIn(category, "");
      s"""trackVirtualPageView("$newCategory");""".toString
    } else {
      ""
    }
  }
}
