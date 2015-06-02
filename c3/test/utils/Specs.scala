package utils

import org.openqa.selenium.WebDriver
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import play.api.test.FakeApplication

abstract class WithApplication(app: FakeApplication = LightFakeApplication()) extends play.api.test.WithApplication(app)

abstract class WithServer(app: FakeApplication = LightFakeApplication()) extends play.api.test.WithServer(app)

abstract class WithBrowser[WEBDRIVER <: WebDriver](app: FakeApplication = LightFakeApplication())
  extends play.api.test.WithBrowser(WebDriverHelper.createDefaultWebDriver(false), app)

abstract class WithJsBrowser[WEBDRIVER <: WebDriver](app: FakeApplication = LightFakeApplication())
  extends play.api.test.WithBrowser(WebDriverHelper.createDefaultWebDriver(true), app)

object WebDriverHelper {
  def createDefaultWebDriver(enableJs: Boolean = false): WebDriver = {
    new HtmlUnitDriver(enableJs)
  }
}