package utils.pageobjects.s_about_you

import utils.WithBrowser
import utils.pageobjects._

/**
 * PageObject for page s_about_you g_yourDetails.
 * @author Jorge Migueis
 *         Date: 09/07/2013
 */
final class GYourDetailsPage(ctx:PageObjectsContext) extends ClaimPage(ctx, GYourDetailsPage.url) {
  declareRadioList("#title", "AboutYouTitle")
  declareInput("#titleOther", "AboutYouTitleOther")
  declareInput("#firstName","AboutYouFirstName")
  declareInput("#middleName","AboutYouMiddleName")
  declareInput("#surname","AboutYouSurname")
  declareNino("#nationalInsuranceNumber","AboutYouNINO")
  declareDate("#dateOfBirth", "AboutYouDateOfBirth")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object GYourDetailsPage {
  val url  = "/about-you/your-details"

  def apply(ctx:PageObjectsContext) = new GYourDetailsPage(ctx)
}

/** The context for Specs tests */
trait GYourDetailsPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GYourDetailsPage (PageObjectsContext(browser))
}
