package utils.pageobjects.s2_about_you

import play.api.test.WithBrowser
import utils.pageobjects._

final class G4NationalityAndResidencyPage(ctx:PageObjectsContext) extends ClaimPage(ctx, G4NationalityAndResidencyPage.url, G4NationalityAndResidencyPage.title) {
  declareRadioList("#nationality", "AboutYouNationalityAndResidencyNationality")
  declareInput("#actualnationality", "AboutYouNationalityAndResidencyActualNationality")
  declareYesNo("#resideInUK_answer", "AboutYouNationalityAndResidencyResideInUK")
  declareInput("#resideInUK_text", "AboutYouNationalityAndResidencyNormalResidency")
}

object G4NationalityAndResidencyPage {
  val title = ("Nationality and where you live - About you - the carer").toLowerCase()

  val url = "/about-you/nationality-and-residency"

  def apply(ctx:PageObjectsContext) = new G4NationalityAndResidencyPage(ctx)
}

/** The context for Specs tests */
trait G4NationalityAndResidencyPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G4NationalityAndResidencyPage (PageObjectsContext(browser))
}