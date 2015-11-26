package utils

import java.util.UUID._

import org.openqa.selenium.htmlunit.HtmlUnitDriver
import play.api.test._
import org.specs2.mutable._
import org.openqa.selenium.WebDriver
import org.specs2.execute.{ AsResult, Result }

// NOTE: Do *not* put any initialisation code in the below classes, otherwise delayedInit() gets invoked twice
// which means around() gets invoked twice and everything is not happy.  Only lazy vals and defs are allowed, no vals
// or any other code blocks.

trait PlaySpecification extends org.specs2.mutable.Specification with play.api.test.PlayRunners with play.api.http.HeaderNames with play.api.http.Status with play.api.http.HttpProtocol with play.api.test.DefaultAwaitTimeout with play.api.test.ResultExtractors with play.api.test.Writeables with play.api.test.RouteInvokers with play.api.test.FutureAwaits {
}

/**
 * Used to run specs within the context of a running application.
 *
 */
abstract class WithApplication(val app: FakeApplication = LightFakeApplication.fa, val claimKey: String = randomUUID.toString) extends Around with org.specs2.matcher.MustThrownExpectations with org.specs2.matcher.ShouldThrownExpectations {
  implicit def implicitApp = app
  override def around[T: AsResult](t: => T): Result = {
    Helpers.running(app)(AsResult.effectively(t))
  }
}

/**
 * Used to run specs within the context of a running server.
 *
 * @param app The fake application
 * @param port The port to run the server on
 */
abstract class WithServer(val app: FakeApplication = LightFakeApplication.fa,
                          val claimKey: String = randomUUID.toString,
                          val port: Int = Helpers.testServerPort) extends Around with org.specs2.matcher.MustThrownExpectations with org.specs2.matcher.ShouldThrownExpectations {
  implicit def implicitApp = app
  implicit def implicitPort: Port = port

  override def around[T: AsResult](t: => T): Result = Helpers.running(TestServer(port, app))(AsResult.effectively(t))
}


abstract class WithJsBrowser[WEBDRIVER <: WebDriver](app: FakeApplication = LightFakeApplication.fa) extends WithBrowsers(WebDriverHelper.createDefaultWebDriver(true), app)

abstract class WithBrowser[WEBDRIVER <: WebDriver](app: FakeApplication = LightFakeApplication.fa) extends WithBrowsers(WebDriverHelper.createDefaultWebDriver(false), app)

/**
 * Used to run specs within the context of a running server, and using a web browser
 *
 * @param webDriver The driver for the web browser to use
 * @param app The fake application
 * @param port The port to run the server on
 */
abstract class WithBrowsers[WEBDRIVER <: WebDriver](
                                                     val webDriver: WebDriver = WebDriverHelper.createDefaultWebDriver(false),
                                                     val app: FakeApplication = LightFakeApplication.fa,
                                                     val claimKey: String = randomUUID.toString,
                                                     val port: Int = Helpers.testServerPort) extends Around with org.specs2.matcher.MustThrownExpectations with org.specs2.matcher.ShouldThrownExpectations {

  def this(
            webDriver: Class[WEBDRIVER],
            app: FakeApplication,
            claimKey: String,
            port: Int) = this(WebDriverFactory(webDriver), app, claimKey, port)

  implicit def implicitApp = app

  implicit def implicitPort: Port = port

  lazy val browser: TestBrowser = TestBrowser(webDriver, Some("http://localhost:" + port))

  override def around[T: AsResult](t: => T): Result = {
    try {
      Helpers.running(TestServer(port, app))(AsResult.effectively(t))
    } finally {
      browser.quit()
    }
  }
}


object WebDriverHelper {
  def createDefaultWebDriver(enableJs: Boolean = false): WebDriver = {
    new HtmlUnitDriver(enableJs)
  }
}
