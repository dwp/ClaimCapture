package controllers

import java.util.concurrent.TimeUnit
import play.api.test.TestBrowser
import org.specs2.matcher.MustMatchers

trait BrowserMatchers extends MustMatchers {
  this: { val browser: TestBrowser }  =>

  def titleMustEqual(title: String) = {
    browser.waitUntil[Boolean](30, TimeUnit.SECONDS) {
      browser.title mustEqual title
    }
  }

  def findMustEqualSize(searchFor: String, expectedSize: Integer) = {
    browser.waitUntil[Boolean](30, TimeUnit.SECONDS) {
      browser.find(searchFor).size() mustEqual expectedSize
    }
  }

  def findMustEqualValue(searchFor: String, expectedValue: String) = {
    browser.waitUntil[Boolean](30, TimeUnit.SECONDS) {
      browser.find(searchFor).getValue mustEqual expectedValue
    }
  }
}