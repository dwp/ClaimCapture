package utils.pageobjects

import play.api.test.TestBrowser
import scala.collection.convert.Wrappers.JListWrapper
import org.openqa.selenium.By
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat


/**
 * Operations on the web elements composing a page.
 * @author Jorge Migueis
 *         Date: 11/07/2013
 */
trait ComponentObject {
  this: {val browser: TestBrowser} =>

  def click(elementCssSelector: String) = browser.click(elementCssSelector)

  def fillDate(elementCssSelector: String, value: String) = if (null != value) {
    val date = DateTime.parse(value, DateTimeFormat.forPattern("dd/MM/yyyy"))
    fillSelect(elementCssSelector + "_day", date.dayOfMonth().getAsText)
    fillSelect(elementCssSelector + "_month", date.monthOfYear().getAsText)
    fillInput(elementCssSelector + "_year", date.year().getAsText)
  }

  def fillInput(elementCssSelector: String, value: String) = if (null != value) browser.fill(elementCssSelector).`with`(value)

  def fillSelect(elementCssSelector: String, value: String) =  if (null != value) {
      val select = browser.find(elementCssSelector, 0).getElement()
      val allOptions = (new JListWrapper(select.findElements(By.tagName("option")))) // Java list
      for (option <- allOptions; if (option.getText == value)) option.click()
    }


  def fillYesNo(elementCssSelector: String, value: String) = if (null != value) browser.click(elementCssSelector + "_" + value.toLowerCase())

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
