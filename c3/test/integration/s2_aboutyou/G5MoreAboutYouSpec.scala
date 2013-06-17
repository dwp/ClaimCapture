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

    "contain claim dates" in new WithBrowser {
      val dateString = "03/04/1950"
      Helper.fillClaimDate(browser)
      val h3 = browser.find("div[class=completed] ul li h3")
      h3.getText.contains(dateString) mustEqual true

      val questionLabels = browser.find("fieldset[class=form-elements] ul > li > label")
      questionLabels.get(0).getText.contains(dateString) mustEqual true
      questionLabels.get(3).getText.contains(dateString) mustEqual true
      questionLabels.get(6).getText.contains(dateString) mustEqual true
    }

  } section "integration"

}

