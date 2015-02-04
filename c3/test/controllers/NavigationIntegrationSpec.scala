package controllers

import org.specs2.mutable.Specification
import org.specs2.mutable.Tags
import play.api.test.WithBrowser
import org.fluentlenium.core.Fluent

class NavigationIntegrationSpec extends Specification with Tags {
  "About you" should {
    """navigate to
        "your details" then
        "contact details" and back to
        "your details".""" in new WithBrowser with WithBrowserHelper with BrowserMatchers with DataFiller {
      goTo("/about-you/your-details").title shouldEqual "Your details - About you - the carer"
      fill in `/about-you/your-details`

      next.title shouldEqual "Your contact details - About you - the carer"
      back.title shouldEqual "Your details - About you - the carer"
    }

    """navigate to
        "your details" then
        "contact details" then
        "claim date" (i.e. skipping "time outside UK") then back to
        "contact details" and finally back to
        "your details".""" in new WithBrowser with WithBrowserHelper with BrowserMatchers with DataFiller {
      goTo("/about-you/your-details").title shouldEqual "Your details - About you - the carer"
      fill in `/about-you/your-details`

      next.title shouldEqual "Your contact details - About you - the carer"
      fill in `/about-you/contact-details`

      next.title shouldEqual "Nationality and where you live - About you - the carer"
      back.title shouldEqual "Your contact details - About you - the carer"
      back.title shouldEqual "Your details - About you - the carer"
    }
  } section "integration"
}

trait DataFiller {
  this: WithBrowserHelper =>

  def `/about-you/your-details`: Fluent = {
    click("#title option[value='Mr']")
    fill("#firstName") `with` "Scooby"
    fill("#surname") `with` "Doo"
    fill("#nationalInsuranceNumber_nino") `with` "AB123456C"
    fill("#dateOfBirth_day") `with` "3"
    fill("#dateOfBirth_month") `with` "4"
    fill("#dateOfBirth_year") `with` "2001"
  }

  def `/about-you/contact-details`: Fluent = {
    fill("#address_lineOne") `with` "My Address"
    fill("#address_lineTwo") `with` "My Address line 2"
    fill("#postcode") `with` "SE1 6EH"
    fill("#howWeContactYou") `with` "01772 888901"
    fluent
  }
}