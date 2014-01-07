package controllers.s2_about_you

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.ClaimScenarioFactory
import utils.pageobjects.s2_about_you._

class G2ContactDetailsIntegrationSpec extends Specification with Tags {
  "Contact Details" should {
    "be presented" in new WithBrowser with G2ContactDetailsPageContext {
      page goToThePage()
    }

    "contain 1 completed form" in new WithBrowser with G1YourDetailsPageContext {
      val claim = ClaimScenarioFactory yourDetailsWithNotTimeOutside()
      page goToThePage()
      page fillPageWith claim
      page submitPage() match {
        case p: G2ContactDetailsPage =>  {
          p numberSectionsCompleted()  mustEqual 1
          p fillPageWith claim
        }
        case _ => ko("Next Page is not of the right type.")
      }
    }
    
    "navigate to next page on valid submission" in new WithBrowser with G2ContactDetailsPageContext {
      val claim = ClaimScenarioFactory.yourDetailsWithNotTimeOutside()
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()
      
      nextPage must beAnInstanceOf[G3NationalityAndResidencyPage]
    }

    "be able to navigate back to a completed form" in new WithBrowser  with G1YourDetailsPageContext {
      val claim = ClaimScenarioFactory yourDetailsWithNotTimeOutside()
      page goToThePage()
      page fillPageWith claim
      val contactDetailsPage = page submitPage(waitForPage = true)
      val completedPage = contactDetailsPage goToCompletedSection(waitForPage = true)
      completedPage must beAnInstanceOf[G1YourDetailsPage]
    }
  } section("integration", models.domain.AboutYou.id)
}