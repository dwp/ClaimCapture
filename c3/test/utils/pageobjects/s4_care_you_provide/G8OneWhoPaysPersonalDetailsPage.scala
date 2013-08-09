package utils.pageobjects.s4_care_you_provide

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{PageContext, Page}

/**
 * * Page object for s4_care_you_provide g8_one_who_pas_personal_details.
 * @author Saqib Kayani
 *         Date: 29/07/2013
 */
final class G8OneWhoPaysPersonalDetailsPage (browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G8OneWhoPaysPersonalDetailsPage.url, G8OneWhoPaysPersonalDetailsPage.title, previousPage) {
  
    declareInput("#organisation", "AboutTheCareYouProvideOrganisationPaysYou")
    declareSelect("#title", "AboutTheCareYouProvideTitlePersonPaysYou")
    declareInput("#firstName", "AboutTheCareYouProvideFirstNamePersonPaysYou")
    declareInput("#middleName", "AboutTheCareYouProvideMiddleNamePersonCareFor")
    declareInput("#surname", "AboutTheCareYouProvideSurnamePersonPaysYou")
    declareInput("#amount", "AboutTheCareYouProvideHowMuchDoYouGetPaidAWeek")
    declareDate("#startDatePayment", "AboutTheCareYouProvideWhenDidThePaymentsStart")
  
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G8OneWhoPaysPersonalDetailsPage {
  val title = "Details of the person/organisation who pays you - About the care you provide"
  val url  = "/careYouProvide/oneWhoPaysPersonalDetails"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G8OneWhoPaysPersonalDetailsPage(browser,previousPage)
}

/** The context for Specs tests */
trait G8OneWhoPaysPersonalDetailsPageContext extends PageContext {
  this: WithBrowser[_] =>
  val page = G8OneWhoPaysPersonalDetailsPage buildPageWith browser
}