package utils.pageobjects

import play.api.test.TestBrowser
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import scala.collection.convert.Wrappers.JListWrapper
import org.openqa.selenium.{WebDriverException, By}

/**
 * Fill operations on the web elements composing a page.
 * @author Jorge Migueis
 *         Date: 15/07/2013
 */
trait WebFillActions {
  this: {val browser: TestBrowser} =>

  def click(elementCssSelector: String) = browser.click(elementCssSelector)

  def fillAddress(elementCssSelector: String, value: String) = if (null != value) {
    val lines = value.split("\n")
    val extensions = Array("_lineOne", "_lineTwo", "_lineThree")
    for (i <- 0 to lines.size - 1) {
      fillInput(elementCssSelector + extensions(i), lines(i))
    }
  }

  def fillCheck(elementCssSelector: String, value: String) = if (null != value) {
    if ("yes" == value.toLowerCase()) click(elementCssSelector)
  }

  def fillDate(elementCssSelector: String, value: String) = if (null != value) {
    val date = DateTime.parse(value, DateTimeFormat.forPattern("dd/MM/yyyy"))
    val day = date.dayOfMonth().getAsText
    fillSelect(elementCssSelector + "_day", day)
    fillSelect(elementCssSelector + "_month", date.monthOfYear().getAsString)
    fillInput(elementCssSelector + "_year", date.year().getAsText)
  }

  def fillDateFromTo(elementCssSelector: String, from: String, to: String) = if (null != from && null != to) {
    fillDate(elementCssSelector + "_from", from)
    fillDate(elementCssSelector + "_to", to)
  }

  def fillInput(elementCssSelector: String, value: String) = if (null != value) {
    try {
      browser.fill(elementCssSelector).`with`(value)
    }
    catch {
      case e: Exception => throw new PageObjectException("Could not fill " + elementCssSelector + " with value " + value, exception = e)
    }
  }

  def fillNino(elementCssSelector: String, value: String) = if (null != value) {
    val extractor = """(.{2})(.{2})(.{2})(.{2})(.)""".r
    val extractor(n1, n2, n3, n4, n5) = value
    fillInput(elementCssSelector + "_ni1", n1)
    fillInput(elementCssSelector + "_ni2", n2)
    fillInput(elementCssSelector + "_ni3", n3)
    fillInput(elementCssSelector + "_ni4", n4)
    fillInput(elementCssSelector + "_ni5", n5)
  }

  def fillRadioList(listName: String, value: String, sep: String = "_"): Unit = if (null != value) {
    try {
      val radioButtons = browser.find("[name=" + listName + "]")
      var found = false
      for (index <- 1 to radioButtons.size()) if (!found) {
        val name = listName + sep + (if (index < 10) s"0$index" else s"$index")
        val label = browser.find("label[for=" + name + "]").get(0).getText
        if (label == value) {
          browser.click("#" + name)
          found = true
        }
      }
      if (!found) throw new PageObjectException("Option " + value + " is invalid for list " + listName)

    }
    catch {
      case e: Exception => throw new PageObjectException("Could not fill " + listName + " with value " + value, exception = e)
    }

  }

  def fillSelect(elementCssSelector: String, value: String):Unit = if (null != value) {
    try {
      val select = browser.find(elementCssSelector, 0).getElement
      val allOptions = new JListWrapper(select.findElements(By.tagName("option"))) // Java list
      var found = false
      for (option <- allOptions; if option.getAttribute("value").toLowerCase == value.toLowerCase) {
        found = true
        option.click()
      }
      if (!found) throw new PageObjectException("Option " + value + " is invalid for combobox " + elementCssSelector)
    }
    catch {
      case e: Exception => throw new PageObjectException("Could not fill " + elementCssSelector + " with value " + value, exception = e)
    }
  }


  def fillSortCode(elementCssSelector: String, value: String) = if (null != value) {
    val extractor = """(\d{2})-(\d{2})-(\d{2})""".r
    val extractor(n1, n2, n3) = value
    fillInput(elementCssSelector + "_sort1", n1)
    fillInput(elementCssSelector + "_sort2", n2)
    fillInput(elementCssSelector + "_sort3", n3)
  }

  private def fillSelectWithOther(elementCssSelector: String, subSelect: String, value: String) = if (null != value) {
    try {
      val select = browser.find(elementCssSelector + "_" + subSelect, 0).getElement
      val allOptions = new JListWrapper(select.findElements(By.tagName("option"))) // Java list
      allOptions.find(wo => wo.getAttribute("value") == value) match {
        case Some(we) => we.click
        case _ => {
          allOptions.find(_.getText.toLowerCase == "other").get.click
          browser.fill(elementCssSelector + "_other") `with` value
        }
      }
    }
    catch {
      case e: Exception => throw new PageObjectException("Could not fill " + elementCssSelector + " with value " + value, exception = e)
    }
  }

  def fillPaymentFrequency(elementCssSelector: String, value: String) = if (null != value) {
    fillSelectWithOther(elementCssSelector, "frequency", value)
  }

  def fillWhereabouts(elementCssSelector: String, value: String) = if (null != value) {
    fillSelectWithOther(elementCssSelector, "location", value)
  }

  def fillTime(elementCssSelector: String, value: String) = if (null != value) {
    try {
      val extractor = """([^:]*):([^:]*)""".r
      val extractor(hour, minute) = value

      fillSelect(elementCssSelector + "_hour", if (hour.length == 1) s"0$hour" else hour)
      fillSelect(elementCssSelector + "_minutes", if (minute.length == 1) s"0$minute" else minute)
    }
    catch {
      case e: MatchError => throw new PageObjectException("Could not fill " + elementCssSelector + " with value " + value, exception = e)
    }
  }

  def fillYesNo(elementCssSelector: String, value: String, sep: String = "_") = if (null != value) try {
    browser.click(elementCssSelector + sep + value.toLowerCase)
  }
  catch {
    case e: Exception => throw new PageObjectException("Could not fill " + elementCssSelector + " with value " + value, exception = e)
  }

}
