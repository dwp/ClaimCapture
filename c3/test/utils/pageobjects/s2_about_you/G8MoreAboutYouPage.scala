package utils.pageobjects.s2_about_you

import play.api.test.WithBrowser
import utils.pageobjects._

/**
 * PageObject for page s2_about_you g8_moreAboutYou.
 * @author Jorge Migueis
 *         Date: 17/07/2013
 */
final class
G8MoreAboutYouPage (ctx:PageObjectsContext) extends ClaimPage(ctx, G8MoreAboutYouPage.url, G8MoreAboutYouPage.title) {
  declareRadioList("#maritalStatus", "AboutYouWhatIsYourMaritalOrCivilPartnershipStatus")
  declareYesNo("#hadPartnerSinceClaimDate", "AboutYouHaveYouHadaPartnerSpouseatAnyTime")
  declareYesNo("#beenInEducationSinceClaimDate", "AboutYouHaveYouBeenOnACourseOfEducation")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object G8MoreAboutYouPage {
  val title = "More about you - About you - the carer".toLowerCase

  val url  = "/about-you/more-about-you"

  def apply(ctx:PageObjectsContext) = new G8MoreAboutYouPage(ctx)
}

/** The context for Specs tests */
trait G8MoreAboutYouPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G8MoreAboutYouPage (PageObjectsContext(browser))
}