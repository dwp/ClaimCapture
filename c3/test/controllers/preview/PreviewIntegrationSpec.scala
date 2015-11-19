package controllers.preview

import org.specs2.mutable._
import utils.WithJsBrowser
import utils.pageobjects.PageObjects
import utils.pageobjects.preview.PreviewPage
import utils.pageobjects.s_about_you.GYourDetailsPage
import utils.pageobjects.s_information.GAdditionalInfoPage
import controllers.{BrowserMatchers, ClaimScenarioFactory}
import utils.pageobjects.s_pay_details.GHowWePayYouPage
import utils.pageobjects.s_consent_and_declaration.GDeclarationPage
import utils.helpers.PreviewField._
import play.api.i18n.{MMessages, MessagesApi}
import play.api.Play.current

class PreviewIntegrationSpec extends Specification {
  "Preview" should {
    "be presented" in new WithJsBrowser with PageObjects {
      val page =  PreviewPage(context)
      page goToThePage()
    }

    "navigate back to Additional Info page" in new WithJsBrowser with PageObjects {
      val additionalInfoPage = GAdditionalInfoPage(context)
      val additionalInfoData = ClaimScenarioFactory.s11ConsentAndDeclaration
      additionalInfoPage goToThePage ()
      additionalInfoPage fillPageWith additionalInfoData
      val previewPage = additionalInfoPage submitPage()
      previewPage must beAnInstanceOf[PreviewPage]
    }

    "navigate to Declaration page" in new WithJsBrowser with PageObjects {
       val previewPage = PreviewPage(context)
       previewPage goToThePage()
       val declarationPage = previewPage submitPage()
       declarationPage must beAnInstanceOf[GDeclarationPage]
    }

    "change Next button text to 'Return to summary'" in new WithJsBrowser with PageObjects {
      val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
      val previewPage = PreviewPage(context) goToThePage()
      previewPage must beAnInstanceOf[PreviewPage]
      browser.findFirst(getLinkId("about_you_contact")).click()
      browser.findFirst("button[value='next']").getText mustEqual messagesApi("form.returnToSummary")

    }
  }
  section("preview")
}
