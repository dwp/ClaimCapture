package utils.pageobjects.s3_your_partner

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

/**
 * Page object for s3_your_partner g3_MoreAboutYourPartner.
 * @author Saqib Kayani
 *         Date: 22/07/2013
 */
final class G3MoreAboutYourPartnerPage (browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G3MoreAboutYourPartnerPage.url, G3MoreAboutYourPartnerPage.title, previousPage){
  /**
   * Reads theClaim and interacts with browser to populate page.
   * @param theClaim   Data to use to fill page
   */
  def fillPageWith(theClaim: ClaimScenario) {
    fillYesNo("#startedLivingTogether_afterClaimDate", theClaim.AboutYourPartnerDidYouStartedLivingTogetherAfterClaimDate)
    fillDate("#startedLivingTogether_date", theClaim.AboutYourPartnertheDateWhenYouStartedLivingTogether)
    fillYesNo("#separated_fromPartner", theClaim.AboutYourPartnerHaveYouSeparatedfromYourPartner)
    fillDate("#separated_date", theClaim.AboutYourPartnerwhenDidYouSeparate)
  }
}


/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G3MoreAboutYourPartnerPage {
  val title = "More About Your Partner - Your Partner"
  val url  = "/yourPartner/moreAboutYourPartner"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G3MoreAboutYourPartnerPage(browser,previousPage)
}

/** The context for Specs tests */
trait G3MoreAboutYourPartnerPageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = G3MoreAboutYourPartnerPage buildPageWith browser
}