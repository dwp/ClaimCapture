package integration.s2_aboutyou

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser

class G3TimeOutsideUKSpec extends Specification with Tags {
  "Time outside UK" should {
    """not request "date of arrival" upon presentation""" in new WithBrowser {
      browser.goTo("/aboutyou/timeOutsideUK")
      browser.$("#livingInUK").getAttribute("style") must contain("none")
    }

    """not request "date of arrival" for carer currently living in UK""" in new WithBrowser {
      browser.goTo("/aboutyou/timeOutsideUK")
      browser.click("#currentlyLivingInUK_yes")
      browser.$("#livingInUK").getAttribute("style") must contain("none")
    }

   /*
    ""not request "date of arrival" when carer has already indicated that they are "currently living in UK"""" in new WithBrowser {
      browser.goTo("/aboutyou/timeOutsideUK")
      browser.click("#currentlyLivingInUK_yes")
      browser.submit("button[type='submit']")
      browser.$("#livingInUK").getAttribute("style") must contain("none")
    }
    """request "date of arrival" for carer not currently living in UK""" in new WithBrowser {
      browser.goTo("/aboutyou/timeOutsideUK")
      browser.click("#currentlyLivingInUK_no")
      browser.$("#livingInUK").getAttribute("style") must contain("block")
    }

    """include "time outside UK" when said carer has not "always lived in UK"""" in new WithBrowser {
      browser.goTo("/aboutyou/yourDetails")
      browser.click("#alwaysLivedUK_no")
      browser.$("#timeOutsideUK").getAttribute("style") must not contain("none")
    }

    """include "time outside Uk" when said carer has already indicated that they have not "always lived in UK"""" in new WithBrowser {
      browser.goTo("/aboutyou/yourDetails")
      browser.click("#alwaysLivedUK_no")
      browser.submit("button[type='submit']")
      browser.$("#timeOutsideUK").getAttribute("style") must contain("block")
    }*/
  } section "integration"
}