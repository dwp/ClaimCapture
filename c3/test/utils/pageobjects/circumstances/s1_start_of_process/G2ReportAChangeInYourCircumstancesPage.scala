package utils.pageobjects.circumstances.s1_start_of_process

import utils.WithBrowser
import utils.pageobjects.{PageObjectsContext, CircumstancesPage, PageContext}

final class G2ReportAChangeInYourCircumstancesPage(ctx:PageObjectsContext) extends CircumstancesPage(ctx, G2ReportAChangeInYourCircumstancesPage.url) {
  declareInput("#fullName","CircumstancesAboutYouFullName")
  declareNino("#nationalInsuranceNumber","CircumstancesAboutYouNationalInsuranceNumber")
  declareDate("#dateOfBirth", "CircumstancesAboutYouDateOfBirth")
  declareInput("#theirFullName","CircumstancesAboutYouTheirFullName")
  declareInput("#theirRelationshipToYou","CircumstancesAboutYouTheirRelationshipToYou")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G2ReportAChangeInYourCircumstancesPage {
  val url  = "/circumstances/identification/about-you"

  def apply(ctx:PageObjectsContext) = new G2ReportAChangeInYourCircumstancesPage(ctx)
}

/** The context for Specs tests */
trait G2ReportAChangeInYourCircumstancesPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G2ReportAChangeInYourCircumstancesPage(PageObjectsContext(browser))
}
