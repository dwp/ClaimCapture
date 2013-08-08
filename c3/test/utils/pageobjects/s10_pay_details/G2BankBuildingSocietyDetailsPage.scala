package utils.pageobjects.s10_pay_details

import play.api.test.TestBrowser
import utils.pageobjects._

/**
 * Page object for s10_pay_details, G2BankBuildingSocietyDetailsPage.
 * @author Saqib Kayani
 *         Date: 01/08/2013
 */
final class G2BankBuildingSocietyDetailsPage (browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G2BankBuildingSocietyDetailsPage.url, G2BankBuildingSocietyDetailsPage.title, previousPage) {
 
    declareInput("#accountHolderName", "HowWePayYouNameOfAccountHolder")
    declareInput("#bankFullName", "HowWePayYouFullNameOfBankorBuildingSociety")
    declareSortCode("#sortCode", "HowWePayYouSortCode")
    declareInput("#accountNumber", "HowWePayYouAccountNumber")
    declareInput("#rollOrReferenceNumber", "HowWePayYouBuildingSocietyRollOrReferenceNumber")

}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G2BankBuildingSocietyDetailsPage {
  val title = "Bank Building Society Details - Pay Details"
  val url  = "/payDetails/bankBuildingSocietyDetails"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G2BankBuildingSocietyDetailsPage(browser,previousPage)
}

/** The context for Specs tests */
trait G2BankBuildingSocietyDetailsPageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = G2BankBuildingSocietyDetailsPage buildPageWith browser
}