package utils.pageobjects.circumstances.s1_about_you

import play.api.test.WithBrowser
import utils.pageobjects._


final class G3DetailsOfThePersonYouCareForPage(ctx:PageObjectsContext) extends CircumstancesPage(ctx, G3DetailsOfThePersonYouCareForPage.url, G3DetailsOfThePersonYouCareForPage.title) {
  declareInput("#firstName","CircumstancesDetailsOfThePersonYouCareForFirstName")
  declareInput("#middleName","CircumstancesDetailsOfThePersonYouCareForMiddleName")
  declareInput("#lastName","CircumstancesDetailsOfThePersonYouCareForLastName")
  declareNino("#nationalInsuranceNumber","CircumstancesDetailsOfThePersonYouCareForNationalInsuranceNumber")
  declareDate("#dateOfBirth", "CircumstancesDetailsOfThePersonYouCareForDateOfBirth")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G3DetailsOfThePersonYouCareForPage {
  val title = "Details of the person you care for - About you - the carer".toLowerCase

  val url  = "/circumstances/identification/details-of-the-person-you-care-for"

  def apply(ctx:PageObjectsContext) = new G3DetailsOfThePersonYouCareForPage(ctx)
}

/** The context for Specs tests */
trait G3DetailsOfThePersonYouCareForPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G3DetailsOfThePersonYouCareForPage(PageObjectsContext(browser))
}
