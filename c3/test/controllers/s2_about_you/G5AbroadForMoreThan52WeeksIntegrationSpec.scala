package controllers.s2_about_you

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{BrowserMatchers, WithBrowserHelper}

class G5AbroadForMoreThan52WeeksIntegrationSpec extends Specification with Tags {
  "Abroad for more that 52 weeks" should {
    "present" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      goTo("/about-you/abroad-for-more-than-52-weeks")
      titleMustEqual("Time outside of England, Scotland or Wales - About you - the carer")
    }

    "provide for trip entry" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      goTo("/about-you/abroad-for-more-than-52-weeks")
      titleMustEqual("Time outside of England, Scotland or Wales - About you - the carer")

      click("#anyTrips_yes")
      next
      titleMustEqual("Trips - About you - the carer")
    }

    """present "completed" when no more 52 week trips are required""" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      goTo("/about-you/abroad-for-more-than-52-weeks")
      titleMustEqual("Time outside of England, Scotland or Wales - About you - the carer")

      click("#anyTrips_no")
      next
      titleMustEqual("Other EEA State or Switzerland - About you - the carer")
    }

    """go back to "Nationality and Residency".""" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      goTo("/about-you/nationality-and-residency")
      titleMustEqual("Your nationality and residency - About you - the carer")

      fill("#nationality") `with` "British"
      click("#resideInUK_answer_yes")
      next
      titleMustEqual("Time outside of England, Scotland or Wales - About you - the carer")

      back
      titleMustEqual("Your nationality and residency - About you - the carer")
    }

    """remember "no more 52 weeks trips" upon stating "52 weeks trips" and returning""" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      goTo("/about-you/abroad-for-more-than-52-weeks")
      titleMustEqual("Time outside of England, Scotland or Wales - About you - the carer")

      click("#anyTrips_no")
      next
      titleMustEqual("Other EEA State or Switzerland - About you - the carer")

      back
      titleMustEqual("Time outside of England, Scotland or Wales - About you - the carer")
      findFirst("#anyTrips_yes").isSelected should beFalse
      findFirst("#anyTrips_no").isSelected should beTrue
    }
  } section("integration", models.domain.AboutYou.id)
}
