package controllers.circs.s2_additional_info

import play.api.test.WithBrowser
import utils.pageobjects.circumstances.s2_additional_info._
import controllers.CircumstancesScenarioFactory
import org.specs2.mutable.{Tags, Specification}
import utils.pageobjects.circumstances.s3_consent_and_declaration.G1DeclarationPage
import utils.pageobjects.circumstances.s1_about_you.{G3DetailsOfThePersonYouCareForPage, G3DetailsOfThePersonYouCareForPageContext}

class G1OtherChangeInfoIntegrationSpec extends Specification with Tags {

  "Other Change Info" should {

    "be presented" in new WithBrowser with G1OtherChangeInfoPageContext {
      page goToThePage()
    }

    "navigate to previous page" in new WithBrowser with G3DetailsOfThePersonYouCareForPageContext {
      val claim = CircumstancesScenarioFactory.detailsOfThePersonYouCareFor
      page goToThePage()
      page fillPageWith claim

      val otherChangeInfoPage = page submitPage()
      otherChangeInfoPage must beAnInstanceOf[G1OtherChangeInfoPage]
      val prevPage = otherChangeInfoPage.goBack()

      prevPage must beAnInstanceOf[G3DetailsOfThePersonYouCareForPage]
    }

    "navigate to next page" in new WithBrowser with G1OtherChangeInfoPageContext {
      val claim = CircumstancesScenarioFactory.otherChangeInfo
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage ()
      nextPage must beAnInstanceOf[G1DeclarationPage]
    }

  } section("integration", models.domain.CircumstancesIdentification.id)

}
