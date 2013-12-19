package controllers.s2_about_you

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{BrowserMatchers, Formulate}
import play.api.i18n.Messages

class G3NationalityAndResidencyIntegrationSpec extends Specification with Tags {
  sequential

  val urlUnderTest = "/about-you/nationality-and-residency"
  val submitButton = "button[type='submit']"
  val errorDiv = "div[class=validation-summary] ol li"

  "Nationality and Residency" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      browser.goTo(urlUnderTest)
      titleMustEqual(Messages("s2.g3") + " - " + Messages("s2.longName"))
    }

    "contain errors on invalid submission" in new WithBrowser {
      browser.goTo(urlUnderTest)
      browser.submit(submitButton)
      browser.find(errorDiv).size mustEqual 2
    }

  } section("integration", models.domain.AboutYou.id)
}
