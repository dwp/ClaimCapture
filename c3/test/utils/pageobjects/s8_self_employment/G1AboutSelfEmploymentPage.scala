package utils.pageobjects.s8_self_employment

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{PageContext, Page}

final class G1AboutSelfEmploymentPage(browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G1AboutSelfEmploymentPage.url, G1AboutSelfEmploymentPage.title, previousPage) {
  declareYesNo("#areYouSelfEmployedNow", "SelfEmployedAreYouSelfEmployedNow")
  declareDate("#whenDidYouStartThisJob", "SelfEmployedWhenDidYouStartThisJob")
  declareDate("#whenDidTheJobFinish", "SelfEmployedWhenDidTheJobFinish")
  declareYesNo("#haveYouCeasedTrading", "SelfEmployedHaveYouCeasedTrading")
  declareInput("#natureOfYourBusiness", "SelfEmployedNatureofYourBusiness")
}

object G1AboutSelfEmploymentPage {
  val title = "Your job - About self-employment"

  val url = "/self-employment/about-self-employment"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G1AboutSelfEmploymentPage(browser, previousPage)
}

trait G1AboutSelfEmploymentPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G1AboutSelfEmploymentPage buildPageWith browser
}