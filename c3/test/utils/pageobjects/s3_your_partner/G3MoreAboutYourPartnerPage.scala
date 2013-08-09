package utils.pageobjects.s3_your_partner

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{PageContext, Page}

/**
 * Page object for s3_your_partner g3_MoreAboutYourPartner.
 * @author Saqib Kayani
 *         Date: 22/07/2013
 */
final class G3MoreAboutYourPartnerPage (browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G3MoreAboutYourPartnerPage.url, G3MoreAboutYourPartnerPage.title, previousPage){

    declareYesNo("#startedLivingTogether_afterClaimDate", "AboutYourPartnerDidYouStartedLivingTogetherAfterClaimDate")
    declareDate("#startedLivingTogether_date", "AboutYourPartnertheDateWhenYouStartedLivingTogether")
    declareYesNo("#separated_fromPartner", "AboutYourPartnerHaveYouSeparatedfromYourPartner")
    declareDate("#separated_date", "AboutYourPartnerwhenDidYouSeparate")
}


/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G3MoreAboutYourPartnerPage {
  val title = "More About Your Partner/Spouse - About Your Partner/Spouse"
  val url  = "/yourPartner/moreAboutYourPartner"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G3MoreAboutYourPartnerPage(browser,previousPage)
}

/** The context for Specs tests */
trait G3MoreAboutYourPartnerPageContext extends PageContext {
  this: WithBrowser[_] =>
  val page = G3MoreAboutYourPartnerPage buildPageWith browser
}