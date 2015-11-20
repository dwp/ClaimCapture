package models.view

import org.specs2.mutable._
import models.domain.{PreviewModel, ContactDetails, YourDetails}

class NavigationSpec extends Specification {
  "Navigation" should {
    "track a route" in {
      val navigation = Navigation().track(YourDetails)("/about-you/your-details")
      navigation.current.uri shouldEqual "/about-you/your-details"
    }

    "track a route - from a given Call" in {
      val navigation = Navigation().track(YourDetails)(controllers.s_about_you.routes.GYourDetails.present().url)
      navigation.current.uri shouldEqual "/about-you/your-details"
    }

    "go back to previous route" in {
      val route1 = "/about-you/your-details"
      val route2 = "/about-you/contact-details"
      val navigation = Navigation().track(YourDetails)(route1).track(ContactDetails)(route2)

      navigation.current.uri shouldEqual route2
      navigation.previous.uri shouldEqual route1
    }

    "give original back from only one existing route" in {
      val route = "/about-you/your-details"
      val navigation = Navigation().track(YourDetails)(route)

      navigation.previous.uri shouldEqual route
    }

    "track 2 routes, and upon going back and then retracking the second route should still only have 2 routes" in {
      val route1 = "/about-you/your-details"
      val route2 = "/about-you/contact-details"
      val navigation = Navigation().track(YourDetails)(route1).track(ContactDetails)(route2)
      navigation.routes.size shouldEqual 2

      navigation.previous.uri shouldEqual route1

      val renavigation = navigation.track(ContactDetails)(route2)
      renavigation.routes.size shouldEqual 2

      navigation.current.uri shouldEqual route2
    }

    "track 2 routes, and find second by it's type" in {
      val route1 = "/about-you/your-details"
      val route2 = "/about-you/contact-details"
      val navigation = Navigation().track(YourDetails)(route1).track(ContactDetails)(route2)
      navigation.routes.size shouldEqual 2
      val expectedRoute = Route(route2)

      navigation(ContactDetails) must beSome(expectedRoute)
    }

    "track Preview and then start tracking on the alternative field" in {
      val route1 = "/about-you/your-details"
      val route2 = "/about-you/contact-details"
      val navigation = Navigation().track(YourDetails)(route1).track(ContactDetails)(route2)
      navigation.routes.size shouldEqual 2
      val expectedRoute = Route(route2)

      navigation(ContactDetails) must beSome(expectedRoute)

      val previewRoute = "/preview"
      val updatedNav = navigation.track(PreviewModel,true)(previewRoute).track(YourDetails)(route1).track(ContactDetails)(route2)

      updatedNav.routes.size shouldEqual 3
      updatedNav.routesAfterPreview.size shouldEqual 2

      updatedNav.previous mustEqual Route(route1)
    }
  }
}
