package utils.helpers

import play.api.Play
import play.api.Play.current

object GAHelper {

  private def addOpt(s:Option[String]):String = s match{
    case Some(s) if s.nonEmpty => ",\""+s+"\""
    case _ => ""
  }

  def trackEvent(category:String, action:String, label:Option[String]=None, value:Option[String]=None, noninteraction:Option[String]=None):String = {
    if (!Play.isTest) {
      s"""trackEvent("$category","$action"${addOpt(label)}${addOpt(value)}${addOpt(noninteraction)});""".toString
    } else {
      ""
    }
  }

  def trackPageView(category:String):String = {
    if (!Play.isTest) {
      s"""trackVirtualPageView("$category");""".toString
    } else {
      ""
    }
  }
}
