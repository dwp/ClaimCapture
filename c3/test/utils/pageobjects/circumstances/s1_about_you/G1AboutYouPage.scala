package utils.pageobjects.circumstances.s1_about_you

import play.api.test.WithBrowser
import utils.pageobjects.{PageObjectsContext, CircumstancesPage, PageContext, Page}

final class G1AboutYouPage(ctx:PageObjectsContext) extends CircumstancesPage(ctx, G1AboutYouPage.url, G1AboutYouPage.title) {
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
  val title = "Your details - About you - the carer".toLowerCase

  val url  = "/circumstances/identification/about-you"

  def apply(ctx:PageObjectsContext) = new G1AboutYouPage(ctx)
}

/** The context for Specs tests */
trait G1AboutYouPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G1AboutYouPage(PageObjectsContext(browser))
}
