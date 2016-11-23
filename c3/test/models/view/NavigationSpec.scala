package models.view

import org.specs2.mutable._
import models.domain.{PreviewModel, ContactDetails, YourDetails}
import utils.WithApplication

class NavigationSpec extends Specification {
  section("unit")
  "Navigation" should {
    "track a route" in new WithApplication {
      val navigation = Navigation().track(YourDetails)("/about-you/your-details")
      navigation.current.uri shouldEqual "/about-you/your-details"
    }

    "track a route - from a given Call" in new WithApplication {
      val navigation = Navigation().track(YourDetails)(controllers.s_about_you.routes.GYourDetails.present().url)
      navigation.current.uri shouldEqual "/about-you/your-details"
    }

    "go back to previous route" in new WithApplication {
      val route1 = "/about-you/your-details"
      val route2 = "/about-you/contact-details"
      val navigation = Navigation().track(YourDetails)(route1).track(ContactDetails)(route2)

      navigation.current.uri shouldEqual route2
      navigation.previous.uri shouldEqual route1
    }

    "give original back from only one existing route" in new WithApplication {
      val route = "/about-you/your-details"
      val navigation = Navigation().track(YourDetails)(route)

      navigation.previous.uri shouldEqual route
    }

    "track 2 routes, and upon going back and then retracking the second route should still only have 2 routes" in new WithApplication {
      val route1 = "/about-you/your-details"
      val route2 = "/about-you/contact-details"
      val navigation = Navigation().track(YourDetails)(route1).track(ContactDetails)(route2)
      navigation.routes.size shouldEqual 2

      navigation.previous.uri shouldEqual route1

      val renavigation = navigation.track(ContactDetails)(route2)
      renavigation.routes.size shouldEqual 2

      navigation.current.uri shouldEqual route2
    }

    "track 2 routes, and find second by it's type" in new WithApplication {
      val route1 = "/about-you/your-details"
      val route2 = "/about-you/contact-details"
      val navigation = Navigation().track(YourDetails)(route1).track(ContactDetails)(route2)
      navigation.routes.size shouldEqual 2

      navigation(ContactDetails).get.uri mustEqual route2
    }

    "track Preview and then start tracking on the alternative field" in new WithApplication {
      val route1 = "/about-you/your-details"
      val route2 = "/about-you/contact-details"
      val navigation = Navigation().track(YourDetails)(route1).track(ContactDetails)(route2)
      navigation.routes.size shouldEqual 2

      navigation(ContactDetails).get.uri mustEqual route2

      val previewRoute = "/preview"
      val updatedNav = navigation.track(PreviewModel,true)(previewRoute).track(YourDetails)(route1).track(ContactDetails)(route2)

      updatedNav.routes.size shouldEqual 3
      updatedNav.routesAfterPreview.size shouldEqual 2

      updatedNav.previous.uri mustEqual route1
    }
  }
  section("unit")
}
