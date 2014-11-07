package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{ClaimScenarioFactory, WithBrowserHelper, BrowserMatchers, Formulate}
import utils.pageobjects.s1_2_claim_date.G1ClaimDatePage
import utils.pageobjects.{TestData, PageObjects}
import utils.pageobjects.s4_care_you_provide.{G11BreakPage, G1TheirPersonalDetailsPage, G10BreaksInCarePage}

class G10BreaksInCareIntegrationSpec extends Specification with Tags {
  "Breaks from care" should {
    "present" in new WithBrowser with PageObjects {
      G10BreaksInCarePage(context) goToThePage() must beAnInstanceOf[G10BreaksInCarePage]
    }

    """present "Their personal details" when no more breaks are required""" in new WithBrowser with PageObjects {
      val breaksInCare = G10BreaksInCarePage(context) goToThePage()
      val data = new TestData
      data.AboutTheCareYouProvideHaveYouHadAnyMoreBreaksInCare_1 = "no"

      val next = breaksInCare fillPageWith data submitPage()
      next must beAnInstanceOf[G1TheirPersonalDetailsPage]
    }
    
    "display dynamic question text if user answered that they care for this person for 35 hours or more each week before your claim date" in new WithBrowser with PageObjects{
      val breaksInCare = G1ClaimDatePage(context) goToThePage() runClaimWith(ClaimScenarioFactory.s4CareYouProvide(true),G10BreaksInCarePage.title)

      breaksInCare.source() contains "Have you had any breaks from caring for this person since 10 April 2014?" should beTrue

    }
    
    "display dynamic question text if user answered that they did NOT care for this person for 35 hours or more each week before your claim date" in new WithBrowser with PageObjects{
      val breaksInCare = G1ClaimDatePage(context) goToThePage() runClaimWith(ClaimScenarioFactory.s4CareYouProvide(false),G10BreaksInCarePage.title)

      breaksInCare.source() contains "Have you had any breaks from caring for this person since 10 October 2014?" should beTrue
    }

    """not record the "yes/no" answer upon starting to add a new break but "cancel".""" in new WithBrowser with PageObjects {
      val breaksInCare = G10BreaksInCarePage(context) goToThePage()
      val data = new TestData
      data.AboutTheCareYouProvideHaveYouHadAnyMoreBreaksInCare_1 = "yes"

      val next = breaksInCare fillPageWith data submitPage()
      next must beAnInstanceOf[G11BreakPage]

      val back = next.goBack()

      back must beAnInstanceOf[G10BreaksInCarePage]

      back.isElemSelected("#answer_yes") should beFalse
      back.isElemSelected("#answer_no") should beFalse
    }

    """allow a new break to be added but not record the "yes/no" answer""" in new WithBrowser with PageObjects {
      val breaksInCare = G1ClaimDatePage(context) goToThePage() runClaimWith(ClaimScenarioFactory.s4CareYouProvideWithBreaksInCare(),G10BreaksInCarePage.title,upToIteration = 2)

      breaksInCare.isElemSelected("#answer_yes") should beFalse
      breaksInCare.isElemSelected("#answer_no") should beFalse
    }

    """remember "no more breaks" upon stating "no more breaks" and returning to "breaks in care".""" in new WithBrowser with PageObjects {
      val breaksInCare = G10BreaksInCarePage(context) goToThePage()
      val data = new TestData
      data.AboutTheCareYouProvideHaveYouHadAnyMoreBreaksInCare_1 = "no"

      val next = breaksInCare fillPageWith data submitPage()
      next must beAnInstanceOf[G1TheirPersonalDetailsPage]

      val back = next.goBack()
      back must beAnInstanceOf[G10BreaksInCarePage]

      back.isElemSelected("#answer_yes") should beFalse
      back.isElemSelected("#answer_no") should beTrue
    }
  } section("integration", models.domain.CareYouProvide.id)
}