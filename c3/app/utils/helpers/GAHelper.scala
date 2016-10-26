package utils.helpers

import play.api.Play
import play.api.Play.current

object GAHelper {
  val regexIterationId = "/[0-9a-f]{8}-([0-9a-f]{4}-){3}[0-9a-f]{12}".r

  private def addOpt(s: Option[String]): String = s match {
    case Some(opt) if opt.nonEmpty => ",\"" + opt + "\""
    case _ => ""
  }

  def trackEvent(category: String, action: String, label: Option[String] = None, value: Option[String] = None): String = {
    if (!Play.isTest) {
      val newCategory = regexIterationId.replaceFirstIn(category, "");
      s"""trackEvent('$newCategory',\"$action\"${addOpt(label)}${addOpt(value)});""".toString
    } else {
      ""
    }
  }

  def trackPageView(category: String): String = {
    if (!Play.isTest) {
      val newCategory = regexIterationId.replaceFirstIn(category, "");
      s"""trackVirtualPageView("$newCategory");""".toString
    } else {
      ""
    }
  }

  /* Replace d/m/yyyy or d/mm/yyyy or dd/mm/yyyy with @date.
    Also d Month yyyy in english or welsh.
   */
  def replaceDate(errorString: String) = {
    val matchesDDMMYYY = "[0-9]{1,2}/[0-9]{1,2}/[0-9]{4}".r.findFirstMatchIn(errorString)
    val matchesDDSPMONTHSPYYY = "[0-9]{1,2} [A-Z][a-z]* [0-9]{4}".r.findFirstMatchIn(errorString)
    if (matchesDDMMYYY.isDefined) {
      val stripped = errorString.substring(0, matchesDDMMYYY.get.start(0)) + "@date" + errorString.substring(matchesDDMMYYY.get.end(0))
      stripped
    } else if (matchesDDSPMONTHSPYYY.isDefined) {
      val stripped = errorString.substring(0, matchesDDSPMONTHSPYYY.get.start(0)) + "@date" + errorString.substring(matchesDDSPMONTHSPYYY.get.end(0))
      stripped
    }
    else {
      errorString
    }
  }

  // Remove any markup with blanks such as ... You must select:<ol class='validation-message list-bullet'><li>Hospital</li>
  def removeHtml(errorString: String) = {
    val stripped = "<[^>]*>".r.replaceAllIn(errorString, "")
    stripped
  }
}
