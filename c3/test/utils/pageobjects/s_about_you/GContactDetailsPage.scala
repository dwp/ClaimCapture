package utils.pageobjects.s_about_you

import utils.WithBrowser
import utils.pageobjects._

/**
 * To change this template use Preferences | File and Code Templates.
 * @author Jorge Migueis
 *         Date: 16/07/2013
 */
final class GContactDetailsPage(ctx:PageObjectsContext) extends ClaimPage(ctx, GContactDetailsPage.url) {
  declareAddress("#address", "AboutYouAddress")
  declareInput("#postcode", "AboutYouPostcode")
  declareInput("#howWeContactYou", "HowWeContactYou")
  declareCheck("#contactYouByTextphone", "AboutYouContactYouByTextphone")
  declareYesNo("#wantsEmailContact","AboutYouWantsEmailContact")
  declareInput("#mail","AboutYouMail")
  declareInput("#mailConfirmation","AboutYouMailConfirmation")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object GContactDetailsPage {
  val url = "/about-you/contact-details"

  def apply(ctx:PageObjectsContext) = new GContactDetailsPage(ctx)
}

/** The context for Specs tests */
trait GContactDetailsPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GContactDetailsPage (PageObjectsContext(browser))
}
