package controllers.s6_education

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser

class G1YourCourseDetailsIntegrationSpec extends Specification with Tags {

  "Your Course Details Page" should {
    "be presented" in new WithBrowser {
      browser.goTo("/education/yourCourseDetails")
      browser.title mustEqual "Your Course Details - Education"
    }

    "contain errors on invalid submission" in new WithBrowser {
      browser.goTo("/education/yourCourseDetails")
      browser.fill("#startDate_year") `with` "INVALID"
      browser.submit("button[type='submit']")
      browser.find("div[class=validation-summary] ol li").size mustEqual 1
    }

  } section "integration"

}
