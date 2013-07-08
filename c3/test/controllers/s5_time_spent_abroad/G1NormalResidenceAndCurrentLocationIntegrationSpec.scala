package controllers.s5_time_spent_abroad

import org.specs2.mutable.{Tags, Specification}
import controllers.WithBrowserAndMatchers

class G1NormalResidenceAndCurrentLocationIntegrationSpec extends Specification with Tags {
  "Normal residence and current location" should {
    "present" in new WithBrowserAndMatchers {
      browser.goTo("/timeSpentAbroad/normalResidenceAndCurrentLocation")
      titleMustEqual("Normal Residence and Current Location - Time Spent Abroad")
    }
  } section "integration"
}