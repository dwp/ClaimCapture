package controllers.save_for_later

import org.specs2.mutable._
import utils.pageobjects.{PageObjects}
import utils.pageobjects.s_about_you.GContactDetailsPage
import utils.pageobjects.save_for_later.GSaveForLaterPage
import utils.{LightFakeApplication, WithJsBrowser, WithBrowser}

class GNationalityAndResidencySaveSpec extends Specification {
  val inputBritish = Seq("nationality" -> "British", "resideInUK.answer" -> "yes")
  val inputAnotherCountryMissingData = Seq("nationality" -> "Another nationality", "resideInUK.answer" -> "yes")

  "Your nationality and residency" should {

    "contain Save button if saveForLaterEnabled switched on and email set" in new WithBrowser(app = LightFakeApplication(additionalConfiguration = Map("saveForLaterEnabled" -> "true"))) with PageObjects{
      val page=GContactDetailsPage(context)
      page goToThePage()
      page fillPageWith SaveForLaterScenarioFactory.WithEmailSet()
      val nationalityPage = page submitPage()
      nationalityPage.source must contain("Save for later")
    }

    "not contain Save button if saveForLaterEnabled=false" in new WithBrowser(app = LightFakeApplication(additionalConfiguration = Map("saveForLaterEnabled" -> "false"))) with PageObjects{
      val page=GContactDetailsPage(context)
      page goToThePage()
      page fillPageWith SaveForLaterScenarioFactory.WithEmailSet()
      val nationalityPage = page submitPage()
      nationalityPage.source must not contain("Save for later")
    }

    "not contain Save button if email is not set" in new WithBrowser(app = LightFakeApplication(additionalConfiguration = Map("saveForLaterEnabled" -> "false"))) with PageObjects{
      val page=GContactDetailsPage(context)
      page goToThePage()
      page fillPageWith SaveForLaterScenarioFactory.WithNoEmailSet()
      val nationalityPage = page submitPage()
      nationalityPage.source must not contain("Save for later")
    }

    "Save for later page correctly displayed" in new WithJsBrowser with PageObjects {
      val page = GContactDetailsPage(context)
      page goToThePage()
      page fillPageWith SaveForLaterScenarioFactory.WithEmailSet()
      val nationalityPage=page.clickLinkOrButton("#next")
      nationalityPage.source must contain("Save for later")
      nationalityPage.source must contain("id=\"save\"")
      nationalityPage goToThePage()

      val savePage=nationalityPage.clickLinkOrButton("#save")
      savePage.url mustEqual GSaveForLaterPage.url
      savePage.source must contain("Your progress has been saved")
      savePage.source must contain("Continue your application" )
      savePage.source must contain("id=\"continue\"")
    }

  }

  section("integration", models.domain.AboutYou.id)

}