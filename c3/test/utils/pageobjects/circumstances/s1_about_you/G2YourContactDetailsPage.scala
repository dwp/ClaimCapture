package utils.pageobjects.circumstances.s1_about_you

import play.api.test.WithBrowser
import utils.pageobjects._


final class G2YourContactDetailsPage(ctx:PageObjectsContext) extends CircumstancesPage(ctx, G2YourContactDetailsPage.url, G2YourContactDetailsPage.title) {
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

  def apply(ctx:PageObjectsContext) = new G2YourContactDetailsPage(ctx)
}

/** The context for Specs tests */
trait G2YourContactDetailsPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G2YourContactDetailsPage(PageObjectsContext(browser))
}
