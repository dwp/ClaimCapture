package utils.pageobjects.s4_care_you_provide

import play.api.test.WithBrowser
import utils.pageobjects._

/**
 * Page object for s4_care_you_provide g2_their_contact_details.
 * @author Saqib Kayani
 *         Date: 25/07/2013
 */
class G2TheirContactDetailsPage (ctx:PageObjectsContext) extends ClaimPage(ctx, G2TheirContactDetailsPage.url) {
  declareAddress("#address", "AboutTheCareYouProvideAddressPersonCareFor")
  declareInput("#postcode", "AboutTheCareYouProvidePostcodePersonCareFor")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G2TheirContactDetailsPage {
  val url  = "/care-you-provide/their-contact-details"

  def apply(ctx:PageObjectsContext) = new G2TheirContactDetailsPage(ctx)
}

/** The context for Specs tests */
trait G2TheirContactDetailsPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G2TheirContactDetailsPage (PageObjectsContext(browser))
}