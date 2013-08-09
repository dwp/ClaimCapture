package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{BrowserMatchers, Formulate}

class G10BreaksInCareIntegrationSpec extends Specification with Tags {
  "Breaks in care" should {
    "present" in new WithBrowser with BrowserMatchers {
      browser.goTo("/careYouProvide/breaksInCare")
      titleMustEqual("Breaks in care - About the care you provide")
    }

    """present "completed" when no more breaks are required""" in new WithBrowser with BrowserMatchers {
      browser.goTo("/careYouProvide/breaksInCare")
      browser.click("#answer_no")
      browser.submit("button[value='next']")
      titleMustEqual("Completion - About the care you provide")
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
      titleMustEqual("Breaks in care - About the care you provide")
      
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
      titleMustEqual("Breaks in care - About the care you provide")
      
      browser.find("ul[class=group] li p").getText mustEqual "* Have you had any breaks in caring since 03/04/1950?"
    }

    """allow a new break to be added but not to record the "yes/no" answer""" in new WithBrowser with BrowserMatchers {
      browser.goTo("/careYouProvide/breaksInCare")
      titleMustEqual("Breaks in care - About the care you provide")

      browser.click("#answer_yes")
      browser.submit("button[value='next']")
      titleMustEqual("Break - About the care you provide")

      browser.click("#backButton")
      titleMustEqual("Breaks in care - About the care you provide")
    }
  } section("integration", models.domain.CareYouProvide.id)
}