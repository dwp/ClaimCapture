package utils.pageobjects.s_care_you_provide

import utils.WithBrowser
import utils.pageobjects._

/**
 * Page object for s_care_you_provide g2_their_contact_details.
 * @author Saqib Kayani
 *         Date: 25/07/2013
 */
class GTheirContactDetailsPage (ctx:PageObjectsContext) extends ClaimPage(ctx, GTheirContactDetailsPage.url) {
  declareAddress("#address", "AboutTheCareYouProvideAddressPersonCareFor")
  declareInput("#postcode", "AboutTheCareYouProvidePostcodePersonCareFor")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object GTheirContactDetailsPage {
  val url  = "/care-you-provide/their-contact-details"

  def apply(ctx:PageObjectsContext) = new GTheirContactDetailsPage(ctx)
}

/** The context for Specs tests */
trait GTheirContactDetailsPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GTheirContactDetailsPage (PageObjectsContext(browser))
}