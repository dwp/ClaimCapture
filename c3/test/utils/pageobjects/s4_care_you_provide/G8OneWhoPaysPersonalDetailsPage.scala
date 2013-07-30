package utils.pageobjects.s4_care_you_provide

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

/**
 * * Page object for s4_care_you_provide g8_one_who_pas_personal_details.
 * @author Saqib Kayani
 *         Date: 29/07/2013
 */
final class G8OneWhoPaysPersonalDetailsPage (browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G8OneWhoPaysPersonalDetailsPage.url, G8OneWhoPaysPersonalDetailsPage.title, previousPage) {
  /**
   * Reads theClaim and interacts with browser to populate page.
   * @param theClaim   Data to use to fill page
   */
  def fillPageWith(theClaim: ClaimScenario) {
    fillInput("#organisation", theClaim.AboutTheCareYouProvideOrganisationPaysYou)
    fillSelect("#title", theClaim.AboutTheCareYouProvideTitlePersonPaysYou)
    fillInput("#firstName", theClaim.AboutTheCareYouProvideFirstNamePersonPaysYou)
    fillInput("#middleName", theClaim.AboutTheCareYouProvideMiddleNamePersonCareFor)
    fillInput("#surname", theClaim.AboutTheCareYouProvideSurnamePersonPaysYou)
    fillInput("#amount", theClaim.AboutTheCareYouProvideHowMuchDoYouGetPaidAWeek)
    fillDate("#startDatePayment", theClaim.AboutTheCareYouProvideWhenDidThePaymentsStart)
  }
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G8OneWhoPaysPersonalDetailsPage {
  val title = "One Who Pays You - Care You Provide"
  val url  = "/careYouProvide/oneWhoPaysPersonalDetails"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G8OneWhoPaysPersonalDetailsPage(browser,previousPage)
}

/** The context for Specs tests */
trait G8OneWhoPaysPersonalDetailsPageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = G8OneWhoPaysPersonalDetailsPage buildPageWith browser
}