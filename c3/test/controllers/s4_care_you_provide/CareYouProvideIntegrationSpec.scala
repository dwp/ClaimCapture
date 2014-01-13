package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{BrowserMatchers, Formulate}

class CareYouProvideIntegrationSpec extends Specification with Tags {

  "Care you provide" should {
    """be presented""" in new WithBrowser with BrowserMatchers {
      Formulate.theirPersonalDetails(browser)
      titleMustEqual("Contact details of the person you care for - About the care you provide")

      browser.goTo("/care-you-provide/completed")
      titleMustEqual("Completion - About the care you provide")
    }

    """navigate to Education""" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      titleMustEqual("Your nationality and residency - About you - the carer")

      Formulate.theirPersonalDetails(browser)
      titleMustEqual("Contact details of the person you care for - About the care you provide")

      Formulate.theirContactDetails(browser)
      titleMustEqual("Relationship and other claims - About the care you provide")

      Formulate.moreAboutThePersonWithClaimedAllowanceBefore(browser)
      titleMustEqual("More about the care you provide - About the care you provide")

      Formulate.moreAboutTheCareWithNotSpent35HoursCaringBeforeClaim(browser)
      titleMustEqual("More about the care you provide - About the care you provide")

      browser.goTo("/care-you-provide/completed")
      titleMustEqual("Completion - About the care you provide")

      browser.find("button[type='submit']").getText shouldEqual "Continue"

      browser.submit("button[type='submit']")
      titleMustEqual("Your course details - About your education")
    }

    """navigate to Self Employment""" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      titleMustEqual("Your nationality and residency - About you - the carer")

      Formulate.nationalityAndResidency(browser)
      titleMustEqual("Details of time abroad for more than 52 weeks - About you - the carer")

      Formulate.otherEEAStateOrSwitzerland(browser)
      titleMustEqual("More about you - About you - the carer")

      Formulate.moreAboutYouNotBeenInEducationSinceClaimDate(browser)
      titleMustEqual("Employment - About you - the carer")

      Formulate.theirPersonalDetails(browser)
      titleMustEqual("Contact details of the person you care for - About the care you provide")

      Formulate.theirContactDetails(browser)
      titleMustEqual("Relationship and other claims - About the care you provide")

      Formulate.moreAboutThePersonWithClaimedAllowanceBefore(browser)
      titleMustEqual("More about the care you provide - About the care you provide")

      Formulate.moreAboutTheCareWithNotSpent35HoursCaringBeforeClaim(browser)
      titleMustEqual("More about the care you provide - About the care you provide")

      browser.goTo("/care-you-provide/completed")
      titleMustEqual("Completion - About the care you provide")

      browser.find("button[type='submit']").getText shouldEqual "Continue"

      browser.submit("button[type='submit']")
      titleMustEqual("Your job - About self-employment")
    }

    """navigate to Other Money""" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      titleMustEqual("Your nationality and residency - About you - the carer")

      Formulate.nationalityAndResidency(browser)
      titleMustEqual("Details of time abroad for more than 52 weeks - About you - the carer")

      Formulate.otherEEAStateOrSwitzerland(browser)
      titleMustEqual("More about you - About you - the carer")

      Formulate.moreAboutYouNotBeenInEducationSinceClaimDate(browser)
      titleMustEqual("Employment - About you - the carer")

      Formulate.notInEmployment(browser)
      titleMustEqual("Completion - About you - the carer")

      Formulate.theirPersonalDetails(browser)
      titleMustEqual("Contact details of the person you care for - About the care you provide")

      Formulate.theirContactDetails(browser)
      titleMustEqual("Relationship and other claims - About the care you provide")

      Formulate.moreAboutThePersonWithClaimedAllowanceBefore(browser)
      titleMustEqual("More about the care you provide - About the care you provide")

      Formulate.moreAboutTheCareWithNotSpent35HoursCaringBeforeClaim(browser)
      titleMustEqual("More about the care you provide - About the care you provide")

      browser.goTo("/care-you-provide/completed")
      titleMustEqual("Completion - About the care you provide")

      browser.find("button[type='submit']").getText shouldEqual "Continue"

      browser.submit("button[type='submit']")
      titleMustEqual("Details about other money - About Other Money")
    }

  } section("integration", models.domain.CareYouProvide.id)
}