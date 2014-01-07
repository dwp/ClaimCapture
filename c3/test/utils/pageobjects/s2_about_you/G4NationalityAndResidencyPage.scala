package utils.pageobjects.s2_about_you

import play.api.i18n.Messages
import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{ClaimPage,Page, PageContext}

final class G4NationalityAndResidencyPage(browser: TestBrowser, previousPage: Option[Page] = None) extends ClaimPage(browser, G4NationalityAndResidencyPage.url, G4NationalityAndResidencyPage.title, previousPage) {
  declareInput("#nationality", "AboutYouNationalityAndResidencyNationality")
  declareYesNo("#resideInUK_answer", "AboutYouNationalityAndResidencyResideInUK")
  declareInput("#resideInUK_text", "AboutYouNationalityAndResidencyNormalResidency")
}

object G4NationalityAndResidencyPage {
  val title = ("Your nationality and residency - About you - the carer").toLowerCase()

  val url = "/about-you/nationality-and-residency"

  def apply(browser: TestBrowser, previousPage: Option[Page] = None) = new G4NationalityAndResidencyPage(browser, previousPage)
}

/** The context for Specs tests */
trait G4NationalityAndResidencyPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G4NationalityAndResidencyPage (browser)
}