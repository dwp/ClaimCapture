package utils.pageobjects

import org.specs2.specification.Scope
import utils.WithBrowsers

trait PageObjects extends Scope {
  this: WithBrowsers[_] =>

  val context = PageObjectsContext(browser)

}
