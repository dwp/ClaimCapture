package utils.pageobjects.s2_about_you

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{PageContext, Page}

/**
 * PageObject for page s2_about_you g5_moreAboutYou.
 * @author Jorge Migueis
 *         Date: 17/07/2013
 */
final class G5MoreAboutYouPage (browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G5MoreAboutYouPage.url, G5MoreAboutYouPage.title, previousPage) {
  declareYesNo("#hadPartnerSinceClaimDate", "AboutYouHaveYouHadaPartnerSpouseatAnyTime")
  declareYesNo("#eitherClaimedBenefitSinceClaimDate", "AboutYouHaveYouOrYourPartnerSpouseClaimedorReceivedAnyOtherBenefits")
  declareYesNo("#beenInEducationSinceClaimDate", "AboutYouHaveYouBeenOnACourseOfEducation")
  declareYesNo("#receiveStatePension", "AboutYouDoYouGetStatePension")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object G5MoreAboutYouPage {
  val title = "More about you - About You"

  val url  = "/about-you/more-about-you"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G5MoreAboutYouPage(browser,previousPage)
}

/** The context for Specs tests */
trait G5MoreAboutYouPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G5MoreAboutYouPage buildPageWith browser
}