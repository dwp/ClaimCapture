package controllers.save_for_later

import controllers.save_for_later.SaveForLaterScenarioFactory._
import org.specs2.mutable._
import utils.pageobjects.PageObjects
import utils.pageobjects.s_about_you.GContactDetailsPage
import utils.{LightFakeApplication,WithBrowser}

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
  }
  section("integration", models.domain.AboutYou.id)
}