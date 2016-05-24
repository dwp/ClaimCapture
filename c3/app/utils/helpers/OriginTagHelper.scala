package utils.helpers

import app.ConfigProperties._

/**
 * Created by peterwhitehead on 25/01/2016.
 */
object OriginTagHelper {
  def isOriginGB() : Boolean = {
    getStringProperty("origin.tag") match {
      case "GB" => true
      case _ => false
    }
  }

  def analyticsTag() : String = {
    isOriginGB match {
      case true => getStringProperty("analytics.accountTag")
      case false => getStringProperty("analytics.accountNITag")
    }
  }
}
