package integration.s2_aboutyou

import play.api.test.WithBrowser
import org.specs2.mutable.{Tags, Specification}
import integration.Helper

class G5MoreAboutYouSpec extends Specification with Tags {

  "More About You" should {
    "present Benefits when there is no claim date" in new WithBrowser {
      browser.goTo("/aboutyou/moreAboutYou")
      browser.title() mustEqual "Benefits - Carer's Allowance"
    }

    "be presented when there is a claim date" in new WithBrowser {
      Helper.fillClaimDate(browser)
      browser.title() mustEqual "More About You - About You"
    }

    "contain the completed forms" in new WithBrowser {
      Helper.fillYourDetails(browser)
      Helper.fillContactDetails(browser)
      Helper.fillClaimDate(browser)
      browser.title() mustEqual "More About You - About You"
      browser.find("div[class=completed] ul li").size() mustEqual 3
    }

    "contain questions with claim dates" in new WithBrowser {
      val dateString = "03/04/1950"
      Helper.fillClaimDate(browser)
      val h3 = browser.find("div[class=completed] ul li h3")
      h3.getText.contains(dateString) mustEqual true

      val questionLabels = browser.find("fieldset[class=form-elements] ul[class=group] > li > label")
      questionLabels.get(0).getText must contain(dateString)
      questionLabels.get(1).getText must contain(dateString)
      questionLabels.get(2).getText must contain(dateString)
    }

    "contain errors on invalid submission" in new WithBrowser {
      Helper.fillClaimDate(browser)
      browser.goTo("/aboutyou/moreAboutYou")
      browser.title() mustEqual "More About You - About You"
      browser.submit("button[type='submit']")

      browser.find("p[class=error]").size mustEqual 4
    }

    "navigate to next page on valid submission" in new WithBrowser {
      Helper.fillClaimDate(browser)
      Helper.fillMoreAboutYou(browser)
      browser.title() mustEqual "Employment - About You"
    }
  } section "integration"
}