package integration.s2_aboutyou

import org.specs2.mutable.{Tags, Specification}

class G3TimeOutsideUKSpec extends Specification with Tags {

  "Time outside UK" should {


    /*"""not include "time outside UK" when "always lived in UK" has not been answered (yet)""" in new WithBrowser {
      browser.goTo("/aboutyou/yourDetails")
      browser.$("#timeOutsideUK").getAttribute("style") must contain("none")
    }

    """not include "time outside UK" when said carer has "always lived in UK"""" in new WithBrowser {
      browser.goTo("/aboutyou/yourDetails")
      browser.click("#alwaysLivedUK_yes")
      browser.$("#timeOutsideUK").getAttribute("style") must /*contain("display") and*/ contain("none")
    }

    """not include "time outside UK" when said carer has already indicated that they have "always lived in UK"""" in new WithBrowser {
      browser.goTo("/aboutyou/yourDetails")
      browser.click("#alwaysLivedUK_yes")
      browser.submit("button[type='submit']")
      browser.$("#timeOutsideUK").getAttribute("style") must contain("none")
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