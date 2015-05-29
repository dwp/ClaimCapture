package controllers

import java.util.concurrent.TimeUnit
import utils.WithBrowser
import org.specs2.matcher.MustMatchers
import scala.util.Try

trait BrowserMatchers extends MustMatchers {
  this: WithBrowser[_] =>

  val duration = Try(System.getProperty("waitSeconds", "30").toInt).getOrElse(30)

  def urlMustEqual(url: String) = {
    browser.waitUntil[Boolean](duration, TimeUnit.SECONDS) {
      (browser.url == url || browser.url.replaceFirst("/[^/]*$","") == url) must beTrue
    }
  }

  def urlMustNotEqual(url: String) = {
    browser.waitUntil[Boolean](duration, TimeUnit.SECONDS) {
      browser.url mustNotEqual url
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