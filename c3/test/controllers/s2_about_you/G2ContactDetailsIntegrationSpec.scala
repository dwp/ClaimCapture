package controllers.s2_about_you

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.ClaimScenarioFactory
import utils.pageobjects.s2_about_you.{G1YourDetailsPage, G2ContactDetailsPage, G1YourDetailsPageContext, ContactDetailsPageContext}

class G2ContactDetailsIntegrationSpec extends Specification with Tags {
  "Contact Details" should {
    "be presented" in new WithBrowser with ContactDetailsPageContext {
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
//          println(p.source())
        }
        case _ => ko("Next Page is not of the right type.")
      }
    }

    "be able to navigate back to a completed form" in new WithBrowser  with G1YourDetailsPageContext {
      val claim = ClaimScenarioFactory yourDetailsWithNotTimeOutside()
      page goToThePage()
      page fillPageWith claim
      val contactDetailsPage = page submitPage()
      val completedPage = contactDetailsPage goToCompletedSection()
      completedPage must beAnInstanceOf[G1YourDetailsPage]
    }
  } section("integration", models.domain.AboutYou.id)
}