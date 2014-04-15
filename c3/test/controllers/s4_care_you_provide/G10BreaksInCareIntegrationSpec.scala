package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{WithBrowserHelper, BrowserMatchers, Formulate}
import utils.pageobjects.s4_care_you_provide.G10BreaksInCarePage

class G10BreaksInCareIntegrationSpec extends Specification with Tags {
  "Breaks in care" should {
    "present" in new WithBrowser with BrowserMatchers {
      browser.goTo("/care-you-provide/breaks-in-care")
      titleMustEqual(G10BreaksInCarePage.title)
    }

    """present "completed" when no more breaks are required""" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      goTo("/care-you-provide/breaks-in-care")
      browser.click("#answer_no")
      next
      titleMustEqual("Details of the person you care for - About the care you provide")
    }

    "go back to contact details" in new WithBrowser {
      pending("Once 'Contact details' are done, this example must be written")
    }
    
    "display dynamic question text if user answered that they care for this person for 35 hours or more each week before your claim date" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.theirPersonalDetails(browser)
      Formulate.theirContactDetails(browser)
      Formulate.moreAboutTheCare(browser)
      goTo("/care-you-provide/breaks-in-care")
      titleMustEqual(G10BreaksInCarePage.title)
      
      browser.find("ul[class=group] li p").getText mustEqual "* Have you had any breaks in caring since 03/10/1949?"
    }
    
    "display dynamic question text if user answered that they did NOT care for this person for 35 hours or more each week before your claim date" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.theirPersonalDetails(browser)
      Formulate.theirContactDetails(browser)
      Formulate.moreAboutTheCareWithNotSpent35HoursCaringBeforeClaim(browser)
      goTo("/care-you-provide/breaks-in-care")
      titleMustEqual(G10BreaksInCarePage.title)
      
      browser.find("ul[class=group] li p").getText mustEqual "* Have you had any breaks in caring since 03/04/1950?"
    }

    """not record the "yes/no" answer upon starting to add a new break but "cancel".""" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      browser.goTo("/care-you-provide/breaks-in-care")
      titleMustEqual(G10BreaksInCarePage.title)

      browser.click("#answer_yes")
      next
      titleMustEqual("Break - About the care you provide")

      back
      titleMustEqual(G10BreaksInCarePage.title)
      browser.findFirst("#answer_yes").isSelected should beFalse
      browser.findFirst("#answer_no").isSelected should beFalse
    }

    """allow a new break to be added but not record the "yes/no" answer""" in new WithBrowser with BreakFiller with WithBrowserHelper with BrowserMatchers {
      goTo("/care-you-provide/breaks-in-care")
      titleMustEqual(G10BreaksInCarePage.title)

      browser.click("#answer_yes")
      next
      titleMustEqual("Break - About the care you provide")

      break()
      next
      titleMustEqual(G10BreaksInCarePage.title)

      browser.findFirst("#answer_yes").isSelected should beFalse
      browser.findFirst("#answer_no").isSelected should beFalse
    }

    """remember "no more breaks" upon stating "no more breaks" and returning to "breaks in care".""" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      goTo("/care-you-provide/breaks-in-care")
      titleMustEqual(G10BreaksInCarePage.title)

      browser.click("#answer_no")
      next
      titleMustEqual("Details of the person you care for - About the care you provide")

      back
      titleMustEqual(G10BreaksInCarePage.title)
      browser.findFirst("#answer_yes").isSelected should beFalse
      browser.findFirst("#answer_no").isSelected should beTrue
    }
  } section("integration", models.domain.CareYouProvide.id)
}