package utils.pageobjects

import play.api.test.TestBrowser
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import scala.collection.convert.Wrappers.JListWrapper
import org.openqa.selenium.By

/**
 * Fill operations on the web elements composing a page.
 * @author Jorge Migueis
 *         Date: 15/07/2013
 */
trait WebFillActions {
  this: {val browser: TestBrowser} =>

  def click(elementCssSelector: String) = browser.click(elementCssSelector)

  def fillDate(elementCssSelector: String, value: String) = if (null != value) {
    val date = DateTime.parse(value, DateTimeFormat.forPattern("dd/MM/yyyy"))
    val day = date.dayOfMonth().getAsText()
    fillSelect(elementCssSelector + "_day",if (day.length == 1) s"0$day" else day )
    fillSelect(elementCssSelector + "_month", date.monthOfYear().getAsText())
    fillInput(elementCssSelector + "_year", date.year().getAsText)
  }

  def fillInput(elementCssSelector: String, value: String) = if (null != value) browser.fill(elementCssSelector).`with`(value)

  def fillNino(elementCssSelector: String, value: String) = if (null != value) {
    val extractor = """(.{2})(.{2})(.{2})(.{2})(.)""".r
    val extractor(n1,n2,n3,n4,n5) = value
    fillInput(elementCssSelector + "_ni1",n1)
    fillInput(elementCssSelector + "_ni2",n2)
    fillInput(elementCssSelector + "_ni3",n3)
    fillInput(elementCssSelector + "_ni4",n4)
    fillInput(elementCssSelector + "_ni5",n5)
  }

  def fillSelect(elementCssSelector: String, value: String) =  if (null != value) {
    val select = browser.find(elementCssSelector, 0).getElement
    val allOptions = new JListWrapper(select.findElements(By.tagName("option"))) // Java list
    for (option <- allOptions; if option.getText == value) option.click()
  }

  def fillAddress(elementCssSelector: String, value: String) = if (null != value) {
    val lines = value.split("\n")
    val extensions = Array("_lineOne", "_lineTwo", "_lineThree")
    for ( i <- 0 to lines.size - 1 ) {  fillInput(elementCssSelector + extensions(i), lines(i)) }

  }


  def fillYesNo(elementCssSelector: String, value: String, sep: String = "_") = if (null != value) browser.click(elementCssSelector + sep + value.toLowerCase)

}
