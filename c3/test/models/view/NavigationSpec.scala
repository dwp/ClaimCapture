package models.view

import org.specs2.mutable.Specification

class NavigationSpec extends Specification {
  "Navigation" should {
    "track a route" in {
      val navigation = Navigation() track "/about-you/your-details"
      navigation.current shouldEqual "/about-you/your-details"
    }

    "track a route - from a given Call" in {
      val navigation = Navigation() track controllers.s2_about_you.routes.G1YourDetails.present().url
      navigation.current shouldEqual "/about-you/your-details"
    }

    "go back to previous route" in {
      val route1 = "/about-you/your-details"
      val route2 = "/about-you/contact-details"
      val navigation = Navigation() track route1 track route2

      navigation.current shouldEqual route2
      navigation.backup.current shouldEqual route1
    }

    "should give original back from only one existing action" in {
      val route = "/about-you/your-details"
      val navigation = Navigation() track route

      navigation.backup.current shouldEqual route
    }
  }
}