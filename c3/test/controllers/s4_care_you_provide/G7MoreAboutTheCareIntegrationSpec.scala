package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{BrowserMatchers, Formulate}

class G7MoreAboutTheCareIntegrationSpec extends Specification with Tags {
  sequential

  "Representatives For The Person" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      browser.goTo("/care-you-provide/more-about-the-care")
      titleMustEqual("More about the care you provide - About the care you provide")
    }

    "contain errors on invalid submission" in new WithBrowser {
      browser.goTo("/care-you-provide/more-about-the-care")
      browser.submit("button[type='submit']")
      browser.find("div[class=validation-summary] ol li").size mustEqual 2
    }

    "contains errors for optional mandatory data" in new WithBrowser {
      browser.goTo("/care-you-provide/more-about-the-care")
      browser.click("#spent35HoursCaring_yes")
      browser.click("#beforeClaimCaring_answer_yes")
//      browser.click("#hasSomeonePaidYou_yes")
      browser.submit("button[type='submit']")
      browser.find("div[class=validation-summary] ol li").size mustEqual 1
    }

    "navigate back" in new WithBrowser with BrowserMatchers {
      Formulate.theirContactDetails(browser)
      browser.click("#backButton")
      titleMustEqual("Contact details of the person you care for - About the care you provide")
    }


    "start to care for the person to be displayed when back button is clicked" in new WithBrowser {
      Formulate.moreAboutTheCare(browser)
      browser.click("#backButton")
      browser.find("#beforeClaimCaring_date_year").size() mustEqual 1
    }

  } section ("integration", models.domain.CareYouProvide.id)
}