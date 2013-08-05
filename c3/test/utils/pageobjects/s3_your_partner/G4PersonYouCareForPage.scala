package utils.pageobjects.s3_your_partner

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

/**
 * Page object for s3_your_partner g4_PersonYouCareFor.
 * @author Saqib Kayani
 *         Date: 24/07/2013
 */
final class G4PersonYouCareForPage (browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G4PersonYouCareForPage.url, G4PersonYouCareForPage.title, previousPage){
  
    declareYesNo("#isPartnerPersonYouCareFor", "AboutYourPartnerIsYourPartnerThePersonYouAreClaimingCarersAllowancefor")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G4PersonYouCareForPage {
  val title = "Person You Care For - Your Partner"
  val url  = "/yourPartner/personYouCareFor"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G4PersonYouCareForPage(browser,previousPage)
}

/** The context for Specs tests */
trait G4PersonYouCareForPageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = G4PersonYouCareForPage buildPageWith browser
}