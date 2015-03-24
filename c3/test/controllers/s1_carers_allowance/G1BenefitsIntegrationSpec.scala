package controllers.s1_carers_allowance

import models.domain.Benefits
import org.specs2.mutable.{Specification, Tags}
import play.api.test.WithBrowser
import utils.pageobjects.s1_carers_allowance.{G2EligibilityPage, G1BenefitsPage}
import utils.pageobjects.{PageObjects, PageObjectsContext, TestData}

class G1BenefitsIntegrationSpec extends Specification with Tags {
  "Carer's Allowance - Benefits - Integration" should {
    "be presented" in new WithBrowser with PageObjects {
		  val page = G1BenefitsPage(context)
      page goToThePage ()
    }

    "contain a link to gov.uk" in new WithBrowser with PageObjects {
		  val page = G1BenefitsPage(context)
      page goToThePage ()
      page.source must contain("https://www.gov.uk")
    }

    "contain errors on invalid submission" in new WithBrowser with PageObjects {
		  val page = G1BenefitsPage(context)
      val claim = new TestData
      page goToThePage()
      val pageWithErrors = page.submitPage()
      pageWithErrors.listErrors.size mustEqual 1
    }
    
    "accept submit if all mandatory fields are populated" in new WithBrowser with PageObjects {
		  val page = G1BenefitsPage(context)
      val claim = new TestData
      claim.CanYouGetCarersAllowanceWhatBenefitDoesThePersonYouCareForGet = "AA"
      page goToThePage()
      page fillPageWith claim
      page submitPage()
    }

    "warn if answer is 'none of the benefits' to person get one of benefits" in new WithBrowser with PageObjects {
		  val page = G1BenefitsPage(context)
      val claim = new TestData
      claim.CanYouGetCarersAllowanceWhatBenefitDoesThePersonYouCareForGet = "NONE"
      page goToThePage()
      page fillPageWith claim
      page visible("#answerNoMessageWrap") must beTrue
    }

    "navigate to next page on valid submission with 'PIP' selected " in new WithBrowser with PageObjects {
		   verifyAnswerMessageAndSubmit(Benefits.pip, context)
    }

    "navigate to next page on valid submission with 'DLA' selected " in new WithBrowser with PageObjects {
      verifyAnswerMessageAndSubmit(Benefits.dla, context)
    }

    "navigate to next page on valid submission with 'AA' selected " in new WithBrowser with PageObjects {
      verifyAnswerMessageAndSubmit(Benefits.aa, context)
    }

    "navigate to next page on valid submission with 'CAA' selected " in new WithBrowser with PageObjects {
      verifyAnswerMessageAndSubmit(Benefits.caa, context)
    }

    "navigate to next page on valid submission with 'AFIP' selected " in new WithBrowser with PageObjects {
      verifyAnswerMessageAndSubmit(Benefits.afip, context)
    }

  } section("integration", models.domain.CarersAllowance.id)

  private def verifyAnswerMessageAndSubmit(benefitAnswer:String, context:PageObjectsContext) = {
    val page = G1BenefitsPage(context)
    val claim = new TestData
    claim.CanYouGetCarersAllowanceWhatBenefitDoesThePersonYouCareForGet = benefitAnswer
    page goToThePage()
    page fillPageWith claim

    page visible("#answerNoMessageWrap") must beFalse

    val nextPage = page submitPage()

    nextPage must beAnInstanceOf[G2EligibilityPage]
  }
}