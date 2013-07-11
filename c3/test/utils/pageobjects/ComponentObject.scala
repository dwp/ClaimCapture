package utils.pageobjects

import play.api.test.TestBrowser

/**
 * To change this template use Preferences | File and Code Templates.
 * @author Jorge Migueis
 *         Date: 11/07/2013
 */
trait ComponentObject {
  this: {val browser: TestBrowser} =>

  protected def isCompletedYesNo(location: String, index: Integer, name: String, value: String) = {
    val completed = browser.find(location).get(index).getText()
    completed.contains(name) && completed.contains(value)
  }

  protected def valueOfYesNo(location: String): Option[Boolean] = {
    browser.find(location).getAttribute("value") match {
      case "true" => Some(true)
      case "false" => Some(false)
      case _ => None
    }
  }

}
