package controllers.circs.s3_consent_and_declaration

import play.api.test.WithBrowser
import utils.pageobjects.circumstances.s2_additional_info._
import controllers.CircumstancesScenarioFactory
import org.specs2.mutable.{Tags, Specification}
import utils.pageobjects.circumstances.s3_consent_and_declaration.{G1DeclarationPageContext, G1DeclarationPage}
import utils.pageobjects.circumstances.s1_about_you.{G4CompletedPage, G3DetailsOfThePersonYouCareForPageContext}

class G1DeclarationIntegrationSpec extends Specification with Tags {

  "Declaration" should {

    "be presented" in new WithBrowser with G1DeclarationPageContext {
      page goToThePage()
    }

    "navigate to previous page" in new WithBrowser with G1OtherChangeInfoPageContext {
      page goToThePage()

      val claim = CircumstancesScenarioFactory.declaration
      val otherChangeInfoPage = page runClaimWith (claim, G1DeclarationPage.title)

      otherChangeInfoPage must beAnInstanceOf[G1DeclarationPage]

      val prevPage = otherChangeInfoPage.goBack()

      prevPage must beAnInstanceOf[G1OtherChangeInfoPage]
    }

    "navigate to next page" in new WithBrowser with G1DeclarationPageContext {
      pending("of finishing the circs submission result page")
      val claim = CircumstancesScenarioFactory.otherChangeInfo
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage ()
      nextPage must beAnInstanceOf[G1DeclarationPage]
    }

  } section("integration", models.domain.CircumstancesConsentAndDeclaration.id)

}
