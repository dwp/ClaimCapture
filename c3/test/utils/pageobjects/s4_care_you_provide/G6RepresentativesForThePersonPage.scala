package utils.pageobjects.s4_care_you_provide

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

/**
 * Page object for s4_care_you_provide g6_representatives_for_the_person.
 * @author Saqib Kayani
 *         Date: 25/07/2013
 */
final class G6RepresentativesForThePersonPage (browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G6RepresentativesForThePersonPage.url, G6RepresentativesForThePersonPage.title, previousPage) {
  
    declareYesNo("#you_actForPerson", "AboutTheCareYouProvideDoYouActforthePersonYouCareFor")
    declareSelect("#you_actAs", "AboutTheCareYouProvideYouActAs")
    declareYesNo("#someoneElse_actForPerson", "AboutTheCareYouProvideDoesSomeoneElseActForThePersonYouCareFor")
    declareSelect("#someoneElse_actAs", "AboutTheCareYouProvidePersonActsAs")
    declareInput("#someoneElse_fullName", "AboutTheCareYouProvideFullNameRepresentativesPersonYouCareFor")
  
}


/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G6RepresentativesForThePersonPage {
  val title = "Representatives For The Person - Care You Provide"
  val url  = "/careYouProvide/representativesForPerson"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G6RepresentativesForThePersonPage(browser,previousPage)
}

/** The context for Specs tests */
trait G6RepresentativesForThePersonPageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = G6RepresentativesForThePersonPage buildPageWith browser
}