package controllers

import play.api.test.WithBrowser

trait Navigation {
  this: WithBrowser[_] =>

  def next = browser.submit("button[value='next']")

  def back = browser.click("#backButton")
}