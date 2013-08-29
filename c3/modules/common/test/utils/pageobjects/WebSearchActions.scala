package utils.pageobjects

import play.api.test.TestBrowser


/**
 * Search operations on the web elements composing a page.
 * To be used by assertions in tests.
 * @author Jorge Migueis
 *         Date: 11/07/2013
 */
trait WebSearchActions {
  this: { val browser: TestBrowser } =>

  def isSpecifiedSectionCompleted(index: Integer, name: String, value: String, location: String = "div[class=completed] ul li") = {
    val completed = browser.find(location).get(index).getText
    completed.contains(name) && completed.contains(value)
  }

  def haveSectionsBeenCompleted(location: String = "div[class=completed] ul li") = !browser.find(location).isEmpty

  def numberSectionsCompleted(location: String = "div[class=completed] ul li") = browser.find(location).size()

  def valueOfYesNoElement(location: String, sep: String = "_"): Option[Boolean] = {
    browser.find(location + sep + "yes").getAttribute("value") match {
      case "true" => Some(true)
      case "false" => Some(false)
      case _ => None
    }
  }

  def titleOfSubmitButton = browser.find("#submit").getText

}
