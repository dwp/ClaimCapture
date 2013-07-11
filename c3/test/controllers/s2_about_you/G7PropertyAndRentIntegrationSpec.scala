package controllers.s2_about_you

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{BrowserMatchers, Formulate}

class G7PropertyAndRentIntegrationSpec extends Specification with Tags {

  "Property and Rent" should {

    "present Benefits when there is no claim date" in new WithBrowser {
      browser.goTo("/aboutyou/propertyAndRent")
      browser.title mustEqual "Benefits - Carer's Allowance"
    }

    "be presented when there is a claim date" in new WithBrowser {
      Formulate.claimDate(browser)
      browser.goTo("/aboutyou/propertyAndRent")
      browser.title mustEqual "Property and Rent - About You"
    }

    "contain the completed forms" in new WithBrowser with BrowserMatchers {
      Formulate.yourDetails(browser)
      Formulate.yourContactDetails(browser)
      Formulate.claimDate(browser)
      Formulate.moreAboutYou(browser)
      Formulate.employment(browser)

      findMustEqualSize("div[class=completed] ul li", 5)
    }

    "contain questions with claim dates" in new WithBrowser {
      val dateString = "03/04/1950"
      Formulate.claimDate(browser)
      browser.goTo("/aboutyou/propertyAndRent")
      val h3 = browser.find("div[class=completed] ul li h3")
      h3.getText.contains(dateString) mustEqual true

      val questionLabels = browser.find("fieldset[class=form-elements] ul[class=group] > li > p")
      questionLabels.get(0).getText must contain(dateString)
      questionLabels.get(1).getText must contain(dateString)
    }

    "contain errors on invalid submission" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      browser.goTo("/aboutyou/propertyAndRent")
      browser.title mustEqual "Property and Rent - About You"
      browser.submit("button[type='submit']")

      browser.find("p[class=error]").size mustEqual 2
    }

    "navigate to next page on valid submission" in new WithBrowser {
      Formulate.claimDate(browser)
      Formulate.propertyAndRent(browser)
      browser.title mustEqual "Completion - About You"
    }
  } section "integration"
}