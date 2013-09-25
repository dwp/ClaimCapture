package utils.pageobjects.circumstances.s1_about_you

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{CircumstancesPage, PageContext, Page}


final class G1AboutYouPage(browser: TestBrowser, previousPage: Option[Page] = None) extends CircumstancesPage(browser, G1AboutYouPage.url, G1AboutYouPage.title, previousPage) {
  declareSelect("#title", "CircumstancesAboutYouTitle")
  declareInput("#firstName","CircumstancesAboutYouFirstName")
  declareInput("#middleName","CircumstancesAboutYouMiddleName")
  declareInput("#lastName","CircumstancesAboutYouLastName")
  declareNino("#nationalInsuranceNumber","CircumstancesAboutYouNationalInsuranceNumber")
  declareDate("#dateOfBirth", "CircumstancesAboutYouDateOfBirth")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G1AboutYouPage {
  val title = "Circumstances - About you - the carer".toLowerCase

  val url  = "/circumstances/identification/about-you"

  def apply(browser: TestBrowser, previousPage: Option[Page] = None) = new G1AboutYouPage(browser, previousPage)
}

/** The context for Specs tests */
trait G1AboutYouPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G1AboutYouPage(browser)
}
