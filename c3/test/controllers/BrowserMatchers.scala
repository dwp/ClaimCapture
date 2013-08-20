package controllers

import java.util.concurrent.TimeUnit
import play.api.test.WithBrowser
import org.specs2.matcher.MustMatchers
import scala.util.Try

trait BrowserMatchers extends MustMatchers {
  this: WithBrowser[_] =>

  val duration = Try(System.getProperty("waitSeconds", "20").toInt).getOrElse(20)

  def titleMustEqual(title: String) = {
    browser.waitUntil[Boolean](duration, TimeUnit.SECONDS) {
      browser.title mustEqual title
    }
  }

  def titleMustNotEqual(title: String) = {
    browser.waitUntil[Boolean](duration, TimeUnit.SECONDS) {
      browser.title mustNotEqual title
    }
  }
    
  def findMustEqualSize(searchFor: String, expectedSize: Integer) = {
    browser.waitUntil[Boolean](duration, TimeUnit.SECONDS) {
      browser.find(searchFor).size mustEqual expectedSize
    }
  }

  def findMustEqualValue(searchFor: String, expectedValue: String) = {
    browser.waitUntil[Boolean](duration, TimeUnit.SECONDS) {
      browser.find(searchFor).getValue mustEqual expectedValue
    }
  }
}