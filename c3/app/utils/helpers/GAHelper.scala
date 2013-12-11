package utils.helpers

object GAHelper {

  private def addOpt(s:Option[String]):String = s match{
    case Some(s) if s.nonEmpty => ",\""+s+"\""
    case _ => ""
  }

  def trackEvent(category:String, action:String, label:Option[String]=None, value:Option[String]=None, noninteraction:Option[String]=None):String = {
    s"""trackEvent("$category","$action"${addOpt(label)}${addOpt(value)}${addOpt(noninteraction)});""".toString
  }
}
