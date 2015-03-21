package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{BrowserMatchers, Formulate}

class CareYouProvideIntegrationSpec extends Specification with Tags {

  "Care you provide" should {
    """navigate to Education""" in new WithBrowser with BrowserMatchers {
      pending("Needs to be upgraded to PageObject.")

      Formulate.claimDate(browser)

      Formulate.theirPersonalDetails(browser)
      urlMustEqual("Contact details of the person you care for - About the care you provide")

      Formulate.theirContactDetails(browser)
      urlMustEqual("More about the care you provide - About the care you provide")

      Formulate.moreAboutTheCareWithNotSpent35HoursCaringBeforeClaim(browser)
      urlMustEqual("More about the care you provide - About the care you provide")

      browser.goTo("/care-you-provide/breaks-in-care")
      urlMustEqual("Breaks from care - About the care you provide")
      browser.click("#answer_no")

      browser.find("button[type='submit']").getText shouldEqual "Next"

      browser.submit("button[type='submit']")
      urlMustEqual("Your course details - Education")
    }


    """navigate to Self Employment""" in new WithBrowser with BrowserMatchers {
      pending("Needs to be upgraded to PageObject.")

      Formulate.claimDate(browser)

      Formulate.nationalityAndResidency(browser)
      urlMustEqual("Time outside of England, Scotland or Wales - About you - the carer")

      Formulate.otherEEAStateOrSwitzerland(browser)
      urlMustEqual("Partner details - About your partner")

      Formulate.theirPersonalDetails(browser)
      urlMustEqual("Contact details of the person you care for - About the care you provide")

      Formulate.theirContactDetails(browser)
      urlMustEqual("More about the care you provide - About the care you provide")

      Formulate.moreAboutTheCareWithNotSpent35HoursCaringBeforeClaim(browser)
      urlMustEqual("More about the care you provide - About the care you provide")

      browser.goTo("/care-you-provide/breaks-in-care")
      urlMustEqual("Breaks from care - About the care you provide")
      browser.click("#answer_no")

      browser.find("button[type='submit']").getText shouldEqual "Next"

      Formulate.selfEmployment(browser)
      urlMustEqual("Your job - About self employment")
    }

    """navigate to Other Money""" in new WithBrowser with BrowserMatchers {
      pending("Needs to be upgraded to PageObject.")

      Formulate.claimDate(browser)

      Formulate.nationalityAndResidency(browser)
      urlMustEqual("Time outside of England, Scotland or Wales - About you - the carer")

      Formulate.otherEEAStateOrSwitzerland(browser)
      urlMustEqual("Partner details - About your partner")

      Formulate.theirPersonalDetails(browser)
      urlMustEqual("Contact details of the person you care for - About the care you provide")

      Formulate.theirContactDetails(browser)
      urlMustEqual("More about the care you provide - About the care you provide")

      Formulate.moreAboutTheCareWithNotSpent35HoursCaringBeforeClaim(browser)
      urlMustEqual("More about the care you provide - About the care you provide")

      browser.goTo("/care-you-provide/breaks-in-care")
      urlMustEqual("Breaks from care - About the care you provide")
      browser.click("#answer_no")

      browser.find("button[type='submit']").getText shouldEqual "Next"

      Formulate.notInEmployment(browser)

      urlMustEqual("Statutory pay, benefits and payments")
    }

  } section("integration", models.domain.CareYouProvide.id)
}