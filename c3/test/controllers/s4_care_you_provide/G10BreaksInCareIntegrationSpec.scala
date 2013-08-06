package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{BrowserMatchers, Formulate}

class G10BreaksInCareIntegrationSpec extends Specification with Tags {
  "Has breaks" should {
    "present" in new WithBrowser with BrowserMatchers {
      browser.goTo("/careYouProvide/breaksInCare")
      titleMustEqual("Breaks in care - Care You Provide")
    }

    """present "completed" when no more breaks are required""" in new WithBrowser with BrowserMatchers {
      browser.goTo("/careYouProvide/breaksInCare")
      browser.click("#answer_no")
      browser.submit("button[value='next']")
      titleMustEqual("Completion - Care You Provide")
    }

    "go back to contact details" in new WithBrowser {
      pending("Once 'Contact details' are done, this example must be written")
    }
    
    "display dynamic question text if user answered that they care for this person for 35 hours or more each week before your claim date" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.theirPersonalDetails(browser)
      Formulate.theirContactDetails(browser)
      Formulate.moreAboutThePersonWithClaimedAllowanceBefore(browser)
      Formulate.previousCarerPersonalDetails(browser)
      Formulate.previousCarerContactDetails(browser)
      Formulate.representativesForThePerson(browser)
      Formulate.moreAboutTheCare(browser)
      browser.goTo("/careYouProvide/breaksInCare")
      titleMustEqual("Breaks in care - Care You Provide")
      
      browser.find("ul[class=group] li p").getText mustEqual "* Have you had any breaks in caring since 03/10/1949?"
    }
    
    "display dynamic question text if user answered that they did NOT care for this person for 35 hours or more each week before your claim date" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.theirPersonalDetails(browser)
      Formulate.theirContactDetails(browser)
      Formulate.moreAboutThePersonWithClaimedAllowanceBefore(browser)
      Formulate.previousCarerPersonalDetails(browser)
      Formulate.previousCarerContactDetails(browser)
      Formulate.representativesForThePerson(browser)
      Formulate.moreAboutTheCareWithNotSpent35HoursCaringBeforeClaim(browser)
      browser.goTo("/careYouProvide/breaksInCare")
      titleMustEqual("Breaks in care - Care You Provide")
      
      browser.find("ul[class=group] li p").getText mustEqual "* Have you had any breaks in caring since 03/04/1950?"
    }
  } section "integration"
}