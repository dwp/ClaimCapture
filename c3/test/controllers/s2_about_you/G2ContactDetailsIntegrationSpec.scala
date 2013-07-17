package controllers.s2_about_you

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.ClaimScenarioFactory
import utils.pageobjects.s2_about_you.{YourDetailsPage, ContactDetailsPage, YourDetailsPageContext, ContactDetailsPageContext}

class G2ContactDetailsIntegrationSpec extends Specification with Tags {

  "Contact Details" should {

    "be presented" in new WithBrowser with ContactDetailsPageContext {
      page goToThePage()
    }

    "contain 1 completed form" in new WithBrowser with YourDetailsPageContext {
      val claim = ClaimScenarioFactory yourDetailsWithNotTimeOutside()
      page goToThePage()
      page fillPageWith claim
      page submitPage() match {
        case p: ContactDetailsPage => p numberSectionsCompleted()  mustEqual 1
        case _ => ko("Next Page is not of the right type.")
      }
    }

    "be able to navigate back to a completed form" in new WithBrowser  with YourDetailsPageContext {

      val claim = ClaimScenarioFactory yourDetailsWithNotTimeOutside()
      page goToThePage()
      page fillPageWith claim
      val contactDetailsPage = page submitPage()
      val completedPage = contactDetailsPage goToCompletedSection()
      completedPage must beAnInstanceOf[YourDetailsPage]
    }
  } section "integration"
}