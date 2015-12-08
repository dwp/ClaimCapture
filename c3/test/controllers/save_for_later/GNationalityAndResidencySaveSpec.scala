package controllers.save_for_later

import controllers.save_for_later.SaveForLaterScenarioFactory._
import org.specs2.mutable._
import utils.pageobjects.{PageObjects}
import utils.pageobjects.s_about_you.{GContactDetailsPage}
import utils.{LightFakeApplication,WithBrowser}

class GNationalityAndResidencySaveSpec extends Specification {
  "Your nationality and residency" should {

    "contain Save button if saveForLaterSaveEnabled switched on and email set" in new WithBrowser(app = LightFakeApplication(additionalConfiguration = Map("saveForLaterSaveEnabled" -> "true"))) with PageObjects{
      val page=GContactDetailsPage(context)
      page goToThePage()
      page fillPageWith WithEmailSet()
      val nationalityPage = page submitPage()
      nationalityPage.source must contain("Save for later")
    }

    "not contain Save button if saveForLaterSaveEnabled=false" in new WithBrowser(app = LightFakeApplication(additionalConfiguration = Map("saveForLaterSaveEnabled" -> "false"))) with PageObjects{
      val page=GContactDetailsPage(context)
      page goToThePage()
      page fillPageWith WithEmailSet()
      val nationalityPage = page submitPage()
      nationalityPage.source must not contain("Save for later")
    }

    "not contain Save button if email is not set" in new WithBrowser(app = LightFakeApplication(additionalConfiguration = Map("saveForLaterSaveEnabled" -> "false"))) with PageObjects{
      val page=GContactDetailsPage(context)
      page goToThePage()
      page fillPageWith WithNoEmailSet()
      val nationalityPage = page submitPage()
      nationalityPage.source must not contain("Save for later")
    }
/*
    "display save for later page when click save button" in new WithJsBrowser with PageObjects {
      val page = GContactDetailsPage(context)
      page goToThePage()
      page fillPageWith SaveForLaterScenarioFactory.WithEmailSet()
      println("1=======================================")
      println(page.source)
     val nationalityPage=page.clickLinkOrButton("#next")
      nationalityPage.source must contain("Save for later")
      nationalityPage.source must contain("id=\"save\"")
      nationalityPage goToThePage()
      println("================================")
println(nationalityPage.source)

      val savePage=nationalityPage.clickLinkOrButton("#save")
      savePage.url mustEqual GSaveForLaterPage.url
      savePage.source must contain("Your progress has been saved")
      savePage.source must contain("Continue your application" )
      savePage.source must contain("id=\"continue\"")
    }
    */
/*
    /* BROKEN */
    "re-present screen values when return from save for later" in new WithJsBrowser with PageObjects {
      val page = GContactDetailsPage(context)
      page goToThePage()
      page fillPageWith SaveForLaterScenarioFactory.SpanishLivesInSpain()
      val nationalityPage=page.clickLinkOrButton("#next")
      nationalityPage.source must contain("Save for later")
      nationalityPage.source must contain("id=\"save\"")
      nationalityPage goToThePage()

      val savePage=nationalityPage.clickLinkOrButton("#save")
      savePage.url mustEqual GSaveForLaterPage.url
      savePage.source must contain("Your progress has been saved")
      savePage.source must contain("Continue your application" )
      savePage.source must contain("id=\"continue\"")
      savePage goToThePage()

      val nationalityPageAgain=savePage.clickLinkOrButton("#continue")
//      nationalityPageAgain.url mustEqual GNationalityAndResidencyPage.url
      print( nationalityPageAgain.source)
      // we want to find Spain and Spanish
    }
*/
/* BROKEN
    "re-present claim values when go next and back" in new WithJsBrowser with PageObjects {
      val page = GContactDetailsPage(context)
      page goToThePage()
      page fillPageWith SaveForLaterScenarioFactory.SpanishLivesInSpain()
      val nationalityPage=page.clickLinkOrButton("#next")
      nationalityPage.source must contain("Save for later")
      nationalityPage.source must contain("id=\"save\"")
      nationalityPage goToThePage()

      val savePage=nationalityPage.clickLinkOrButton("#save")
      savePage.url mustEqual GSaveForLaterPage.url
      savePage.source must contain("Your progress has been saved")
      savePage.source must contain("Continue your application" )
      savePage.source must contain("id=\"continue\"")

      val nationalityPageAgain=savePage.clickLinkOrButton("#continue")
      nationalityPageAgain.url mustEqual GNationalityAndResidencyPage.url
      print( nationalityPageAgain.source)
      // we want to find Spain and Spanish

      nationalityPageAgain fillPageWith(SaveForLaterScenarioFactory.FrenchLivesInFrance())

      val abroad52WeeksPage=nationalityPageAgain.clickLinkOrButton("#next")
      abroad52WeeksPage.url mustEqual GAbroadForMoreThan52WeeksPage.url

      val nationalityPageYetAgain=abroad52WeeksPage.clickLinkOrButton("#back" )
      nationalityPageYetAgain.url mustEqual GNationalityAndResidencyPage.url

    }
    */
  }
  section("integration", models.domain.AboutYou.id)
}