package utils.pageobjects.s2_about_you

import play.api.test.WithBrowser
import utils.pageobjects._

/**
 * To change this template use Preferences | File and Code Templates.
 * @author Jorge Migueis
 *         Date: 16/07/2013
 */
final class G3ContactDetailsPage(ctx:PageObjectsContext) extends ClaimPage(ctx, G3ContactDetailsPage.url) {
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
object G3ContactDetailsPage {
  val url = "/about-you/contact-details"

  def apply(ctx:PageObjectsContext) = new G3ContactDetailsPage(ctx)
}

/** The context for Specs tests */
trait G3ContactDetailsPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G3ContactDetailsPage (PageObjectsContext(browser))
}