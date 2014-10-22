package utils.pageobjects

import org.specs2.specification.Scope
import play.api.test.WithBrowser

trait PageObjects extends Scope {
  this: WithBrowser[_] =>

  val context = PageObjectsContext(browser)

}
