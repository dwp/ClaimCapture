package utils.pageobjects.s2_about_you

import play.api.test.TestBrowser
import utils.pageobjects.{PageContext, ClaimScenario, Page}

/**
 * PageObject for page s2_about_you g5_moreAboutYou.
 * @author Jorge Migueis
 *         Date: 17/07/2013
 */
final class MoreAboutYouPage (browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, MoreAboutYouPage.url, MoreAboutYouPage.title, previousPage) {
  /**
   * Sub-class reads theClaim and interacts with browser to populate page.
   * @param theClaim   Data to use to fill page
   */
  def fillPageWith(theClaim: ClaimScenario) {
    fillYesNo("#hadPartnerSinceClaimDate", theClaim.AboutYouHaveYouHadaPartnerSpouseatAnyTime)
    fillYesNo("#eitherClaimedBenefitSinceClaimDate", theClaim.AboutYouHaveYouOrYourPartnerSpouseClaimedorReceivedAnyOtherBenefits)
    fillYesNo("#beenInEducationSinceClaimDate", theClaim.AboutYouHaveYouBeenOnACourseOfEducation)
    fillYesNo("#receiveStatePension", theClaim.AboutYouDoYouGetStatePension)
  }
}



/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object MoreAboutYouPage {
  val title = "More About You - About You"
  val url  = "/aboutyou/moreAboutYou"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new MoreAboutYouPage(browser,previousPage)
}

/** The context for Specs tests */
trait MoreAboutYouPageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = MoreAboutYouPage buildPageWith browser
}

