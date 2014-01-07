package utils.pageobjects.s2_about_you

import play.api.i18n.Messages
import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{ClaimPage,Page, PageContext}

final class G3NationalityAndResidencyPage(browser: TestBrowser, previousPage: Option[Page] = None) extends ClaimPage(browser, G3NationalityAndResidencyPage.url, G3NationalityAndResidencyPage.title, previousPage) {
  declareInput("#nationality", "AboutYouNationalityAndResidencyNationality")
  declareYesNo("#resideInUK_answer", "AboutYouNationalityAndResidencyResideInUK")
  declareInput("#resideInUK_text", "AboutYouNationalityAndResidencyNormalResidency")
}

object G3NationalityAndResidencyPage {
  val title = ("Your nationality and residency - About you - the carer").toLowerCase()

  val url = "/about-you/nationality-and-residency"

  def apply(browser: TestBrowser, previousPage: Option[Page] = None) = new G3NationalityAndResidencyPage(browser, previousPage)
}

/** The context for Specs tests */
trait G3NationalityAndResidencyPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G3NationalityAndResidencyPage (browser)
}