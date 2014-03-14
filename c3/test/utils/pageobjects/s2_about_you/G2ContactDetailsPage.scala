package utils.pageobjects.s2_about_you

import play.api.test.WithBrowser
import utils.pageobjects._

/**
 * To change this template use Preferences | File and Code Templates.
 * @author Jorge Migueis
 *         Date: 16/07/2013
 */
final class G2ContactDetailsPage(ctx:PageObjectsContext) extends ClaimPage(ctx, G2ContactDetailsPage.url, G2ContactDetailsPage.title) {
  declareAddress("#address", "AboutYouAddress")
  declareInput("#postcode", "AboutYouPostcode")
  declareInput("#howWeContactYou", "HowWeContactYou")
  declareYesNo("#contactYouByTextphone", "AboutYouContactYouByTextphone")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object G2ContactDetailsPage {
  val title = "Your contact details - About you - the carer".toLowerCase

  val url = "/about-you/contact-details"

  def apply(ctx:PageObjectsContext) = new G2ContactDetailsPage(ctx)
}

/** The context for Specs tests */
trait G2ContactDetailsPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G2ContactDetailsPage (PageObjectsContext(browser))
}