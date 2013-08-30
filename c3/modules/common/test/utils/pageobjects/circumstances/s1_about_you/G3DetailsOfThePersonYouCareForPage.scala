package utils.pageobjects.circumstances.s1_about_you

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{CircumstancesPage, PageContext, Page}


final class G3DetailsOfThePersonYouCareForPage(browser: TestBrowser, previousPage: Option[Page] = None) extends CircumstancesPage(browser, G3DetailsOfThePersonYouCareForPage.url, G3DetailsOfThePersonYouCareForPage.title, previousPage) {
  declareSelect("#title", "CircumstancesDetailsOfThePersonYouCareForTitle")
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

  val url  = "/circumstances/details-of-the-person-you-care-for"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G3DetailsOfThePersonYouCareForPage(browser, previousPage)
}

/** The context for Specs tests */
trait G3DetailsOfThePersonYouCareForPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G3DetailsOfThePersonYouCareForPage buildPageWith browser
}
