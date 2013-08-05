package utils.pageobjects.s9_self_employment

import play.api.test.TestBrowser
import utils.pageobjects.{PageContext, ClaimScenario, Page}

final class G1AboutSelfEmploymentPage(browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G1AboutSelfEmploymentPage.url, G1AboutSelfEmploymentPage.title, previousPage) {

    declareYesNo("#areYouSelfEmployedNow", "SelfEmployedAreYouSelfEmployedNow")
    declareDate("#whenDidYouStartThisJob", "SelfEmployedWhenDidYouStartThisJob")
    declareDate("#whenDidTheJobFinish", "SelfEmployedWhenDidTheJobFinish")
    declareYesNo("#haveYouCeasedTrading", "SelfEmployedHaveYouCeasedTrading")
    declareInput("#natureOfYourBusiness", "SelfEmployedNatureofYourBusiness")

}

object G1AboutSelfEmploymentPage {
  val title = "Self Employment - About Self Employment"
  val url = "/selfEmployment/aboutSelfEmployment"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G1AboutSelfEmploymentPage(browser, previousPage)
}

trait G1AboutSelfEmploymentPageContext extends PageContext {
  this: {val browser: TestBrowser} =>
  val page = G1AboutSelfEmploymentPage buildPageWith browser
}
