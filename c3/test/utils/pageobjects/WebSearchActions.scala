package utils.pageobjects

import play.api.test.TestBrowser


/**
 * Search operations on the web elements composing a page.
 * @author Jorge Migueis
 *         Date: 11/07/2013
 */
trait WebSearchActions {
  this: {val browser: TestBrowser} =>

  protected def isCompletedYesNo(index: Integer, name: String, value: String, location: String = "div[class=completed] ul li") = {
    val completed = browser.find(location).get(index).getText()
    completed.contains(name) && completed.contains(value)
  }

  protected def valueOfYesNo(location: String, sep: String = "_"): Option[Boolean] = {
    browser.find(location + sep + "yes").getAttribute("value") match {
      case "true" => Some(true)
      case "false" => Some(false)
      case _ => None
    }
  }

}
