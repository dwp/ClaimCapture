package controllers

import play.api.test.WithBrowser
import java.util.concurrent.TimeUnit
import org.specs2.matcher.MustMatchers

abstract class WithBrowserAndMatchers extends WithBrowser with MustMatchers {
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
}