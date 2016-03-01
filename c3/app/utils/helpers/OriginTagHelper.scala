package utils.helpers

import app.ConfigProperties._

/**
 * Created by peterwhitehead on 25/01/2016.
 */
object OriginTagHelper {
  def isOriginGB() : Boolean = {
    getProperty("origin.tag", "GB") match {
      case "GB" => true
      case _ => false
    }
  }

  def analyticsTag() : String = {
    isOriginGB match {
      case true => getProperty("analytics.accountTag","UA-57523228-1")
      case false => getProperty("analytics.accountNITag","UA-57523228-28")
    }
  }
}
