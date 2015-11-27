package controllers.save_for_later

import org.specs2.mutable._
import utils.WithJsBrowser
import utils.pageobjects.PageObjects
import utils.pageobjects.s_about_you.GContactDetailsPage
import utils.pageobjects.save_for_later.GSaveForLaterPage

class GSaveForLaterIntegrationSpec extends Specification {

  "Save for later page" should {

    "be shown after clicking Save in nationality" in new WithJsBrowser with PageObjects {
      val page = GContactDetailsPage(context)
      page goToThePage()
      page fillPageWith SaveForLaterScenarioFactory.WithEmailSet()

      val nationalityPage = page.clickLinkOrButton("#next")
      nationalityPage.source must contain("Save for later")
      nationalityPage.source must contain("id=\"save\"")
      val savePage=nationalityPage.clickLinkOrButton("#save")
      savePage.url mustEqual GSaveForLaterPage.url
      savePage.source must contain("Your progress has been saved")

      savePage.source must contain("Continue your application" )
      savePage.source must contain("id=\"continue\"")
    }
  }

  section("integration", models.domain.AboutYou.id)

}