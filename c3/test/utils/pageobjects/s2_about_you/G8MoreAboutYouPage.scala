package utils.pageobjects.s2_about_you

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{ClaimPage,Page, PageContext}

/**
 * PageObject for page s2_about_you g8_moreAboutYou.
 * @author Jorge Migueis
 *         Date: 17/07/2013
 */
final class G8MoreAboutYouPage (browser: TestBrowser, previousPage: Option[Page] = None) extends ClaimPage(browser, G8MoreAboutYouPage.url, G8MoreAboutYouPage.title, previousPage) {
  declareSelect("#maritalStatus", "AboutYouWhatIsYourMaritalOrCivilPartnershipStatus")
  declareYesNo("#hadPartnerSinceClaimDate", "AboutYouHaveYouHadaPartnerSpouseatAnyTime")
  declareYesNo("#beenInEducationSinceClaimDate", "AboutYouHaveYouBeenOnACourseOfEducation")
  declareYesNo("#receiveStatePension", "AboutYouDoYouGetStatePension")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object G8MoreAboutYouPage {
  val title = "More about you - About you - the carer".toLowerCase

  val url  = "/about-you/more-about-you"

  def apply(browser: TestBrowser, previousPage: Option[Page] = None) = new G8MoreAboutYouPage(browser,previousPage)
}

/** The context for Specs tests */
trait G8MoreAboutYouPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G8MoreAboutYouPage (browser)
}