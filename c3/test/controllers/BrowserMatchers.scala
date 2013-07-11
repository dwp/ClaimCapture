package controllers

import scala.concurrent.duration._
import java.util.concurrent.TimeUnit
import play.api.test.WithBrowser
import org.specs2.matcher.MustMatchers

trait BrowserMatchers extends MustMatchers {
  this: WithBrowser[_] =>

  def titleMustEqual(title: String)(implicit duration: Duration = 10 seconds) = {
    browser.waitUntil[Boolean](duration.toMillis.toInt, TimeUnit.MILLISECONDS) {
      browser.title mustEqual title
    }
  }

  def findMustEqualSize(searchFor: String, expectedSize: Integer)(implicit duration: Duration = 10 seconds) = {
    browser.waitUntil[Boolean](duration.toMillis.toInt, TimeUnit.MILLISECONDS) {
      browser.find(searchFor).size mustEqual expectedSize
    }
  }

  def findMustEqualValue(searchFor: String, expectedValue: String)(implicit duration: Duration = 10 seconds) = {
    browser.waitUntil[Boolean](duration.toMillis.toInt, TimeUnit.MILLISECONDS) {
      browser.find(searchFor).getValue mustEqual expectedValue
    }
  }
}