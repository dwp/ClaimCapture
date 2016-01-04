package controllers.save_for_later

import controllers.ClaimScenarioFactory
import controllers.save_for_later.SaveForLaterScenarioFactory._
import org.specs2.mutable._
import utils.pageobjects.s_claim_date.GClaimDatePage
import utils.pageobjects.save_for_later.{GSaveForLaterSavePage}
import utils.pageobjects.{Page, PageObjectsContext, PageObjects}
import utils.pageobjects.s_about_you.{GNationalityAndResidencyPage, GContactDetailsPage}
import utils.{WithJsBrowser, LightFakeApplication, WithBrowser}


class GNationalityAndResidencySaveSpec extends Specification {
  section("integration", models.domain.AboutYou.id)
  "Your nationality and residency" should {
    "contain Save button if saveForLaterSaveEnabled switched on and email set" in new WithBrowser(app = LightFakeApplication(additionalConfiguration = Map("saveForLaterSaveEnabled" -> "true"))) with PageObjects {
      val page=GContactDetailsPage(context)
      page goToThePage()
      page fillPageWith WithEmailSet()
      val nationalityPage = page submitPage()
      nationalityPage.source must contain("Save for later")
    }

    "not contain Save button if saveForLaterSaveEnabled=false" in new WithBrowser(app = LightFakeApplication(additionalConfiguration = Map("saveForLaterSaveEnabled" -> "false"))) with PageObjects {
      val page=GContactDetailsPage(context)
      page goToThePage()
      page fillPageWith WithEmailSet()
      val nationalityPage = page submitPage()
      nationalityPage.source must not contain("Save for later")
    }

    "not contain Save button if email is not set" in new WithBrowser(app = LightFakeApplication(additionalConfiguration = Map("saveForLaterSaveEnabled" -> "false"))) with PageObjects {
      val page=GContactDetailsPage(context)
      page goToThePage()
      page fillPageWith WithNoEmailSet()
      val nationalityPage = page submitPage()
      nationalityPage.source must not contain("Save for later")
    }

    "display save for later page when click save button" in new WithJsBrowser with PageObjects {
      loadClaimData(context)
      val page = GContactDetailsPage(context)
      page goToThePage()
      page fillPageWith SaveForLaterScenarioFactory.WithEmailSet()

      val nationalityPage = page.clickLinkOrButton("#next")
      nationalityPage.source must contain("Save for later")
      nationalityPage.source must contain("id=\"save\"")

      val savePage=nationalityPage.clickLinkOrButton("#save")
      savePage.url mustEqual GSaveForLaterSavePage.url
      savePage.source must contain("Your progress has been saved")
      savePage.source must contain("Continue your application" )
      savePage.source must contain("id=\"continue\"")
    }

    "re-present screen values when continue/return from save for later screen" in new WithJsBrowser with PageObjects {
      loadClaimData(context)
      val page = GContactDetailsPage(context)
      page goToThePage()
      page fillPageWith SaveForLaterScenarioFactory.WithEmailSet()
      val nationalityPage = page.submitPage()

      nationalityPage.source must contain("Save for later")
      nationalityPage.source must contain("id=\"save\"")
      nationalityPage fillPageWith SaveForLaterScenarioFactory.SpanishLivesInSpain()
      val savePage=nationalityPage.clickLinkOrButton("#save")

      savePage.url mustEqual GSaveForLaterSavePage.url
      savePage.source must contain("Your progress has been saved")
      savePage.source must contain("Continue your application" )
      savePage.source must contain("id=\"continue\"")

      val nationalityPageAgain=savePage.clickLinkOrButton("#continue")
      nationalityPageAgain.url mustEqual GNationalityAndResidencyPage.url
      nationalityPageAgain.source must contain("Spain")
      nationalityPageAgain.source must contain("Spanish")
    }
  }
  section("integration", "SaveForLater")

  def loadClaimData(context:PageObjectsContext):Page = {
    val claimDatePage = GClaimDatePage(context)
    claimDatePage goToThePage()
    val claimDate = ClaimScenarioFactory.s12ClaimDate()
    claimDatePage fillPageWith claimDate

    val page =  claimDatePage submitPage()
    val claim = ClaimScenarioFactory.yourDetailsWithNotTimeOutside()
    page goToThePage()
    page fillPageWith claim
    page submitPage()

  }
}