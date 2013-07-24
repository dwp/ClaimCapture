package controllers.s9_self_employment


import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.s9_self_employment.G1AboutSelfEmploymentPageContext
import utils.pageobjects.ClaimScenario


class G1AboutSelfEmploymentIntegrationSpec extends Specification with Tags{

  "About Self Employment" should {
    "be presented" in new WithBrowser with G1AboutSelfEmploymentPageContext {
      page goToThePage ()
    }

    "contain errors on invalid submission" in new WithBrowser with G1AboutSelfEmploymentPageContext {
      val claim = new ClaimScenario
      claim.SelfEmployedAreYouSelfEmployedNow = ""
      page goToThePage()
      page fillPageWith claim
      val pageWithErrors = page.submitPage()
      pageWithErrors.listErrors.size mustEqual 1
    }
  }

}

