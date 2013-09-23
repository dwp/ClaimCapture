package utils.pageobjects.circumstances.s1_about_you

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{CircumstancesPage, PageContext, Page}


final class G2YourContactDetailsPage(browser: TestBrowser, previousPage: Option[Page] = None) extends CircumstancesPage(browser, G2YourContactDetailsPage.url, G2YourContactDetailsPage.title, previousPage) {
  declareAddress("#address", "CircumstancesYourContactDetailsAddress")
  declareInput("#postcode","CircumstancesYourContactDetailsPostcode")
  declareInput("#phoneNumber","CircumstancesYourContactDetailsPhoneNumber")
  declareInput("#mobileNumber","CircumstancesYourContactDetailsMobileNumber")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G2YourContactDetailsPage {
  val title = "Your contact details - About you - the carer".toLowerCase

  val url  = "/circumstances/identification/your-contact-details"

  def apply(browser: TestBrowser, previousPage: Option[Page] = None) = new G2YourContactDetailsPage(browser, previousPage)
}

/** The context for Specs tests */
trait G2YourContactDetailsPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G2YourContactDetailsPage(browser)
}
