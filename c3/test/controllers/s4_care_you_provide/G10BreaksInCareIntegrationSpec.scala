package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.FormHelper

class G10BreaksInCareIntegrationSpec extends Specification with Tags {
  "Has breaks" should {
    "present" in new WithBrowser {
      browser.goTo("/careYouProvide/breaksInCare")
      browser.title mustEqual "Breaks in Care - Care You Provide"
    }

    """present "completed" when no more breaks are required""" in new WithBrowser {
      browser.goTo("/careYouProvide/breaksInCare")
      browser.click("#answer_no")
      browser.submit("button[value='next']")
      browser.title mustEqual "Completion - Care You Provide"
    }

    "go back to contact details" in new WithBrowser {
      pending("Once 'Contact details' are done, this example must be written")
    }
    
    "display dynamic question text if user answered that they care for this person for 35 hours or more each week before your claim date" in new WithBrowser {
      FormHelper.fillClaimDate(browser)
      FormHelper.fillTheirPersonalDetails(browser)
      FormHelper.fillTheirContactDetails(browser)
      FormHelper.fillMoreAboutThePersonWithClaimedAllowanceBefore(browser)
      FormHelper.fillPreviousCarerPersonalDetails(browser)
      FormHelper.fillPreviousCarerContactDetails(browser)
      FormHelper.fillRepresentativesForThePerson(browser)
      FormHelper.fillMoreAboutTheCare(browser)
      browser.goTo("/careYouProvide/breaksInCare")
      
      browser.find("ul[class=group] li p").getText() mustEqual "* Have you had any breaks in caring since 03/10/1949?"
    }
    
    "display dynamic question text if user answered that they did NOT care for this person for 35 hours or more each week before your claim date" in new WithBrowser {
      FormHelper.fillClaimDate(browser)
      FormHelper.fillTheirPersonalDetails(browser)
      FormHelper.fillTheirContactDetails(browser)
      FormHelper.fillMoreAboutThePersonWithClaimedAllowanceBefore(browser)
      FormHelper.fillPreviousCarerPersonalDetails(browser)
      FormHelper.fillPreviousCarerContactDetails(browser)
      FormHelper.fillRepresentativesForThePerson(browser)
      FormHelper.fillMoreAboutTheCareWithNotSpent35HoursCaringBeforeClaim(browser)
      browser.goTo("/careYouProvide/breaksInCare")
      
      browser.find("ul[class=group] li p").getText() mustEqual "* Have you had any breaks in caring since 03/04/1950?"
    }
  } section "integration"
}