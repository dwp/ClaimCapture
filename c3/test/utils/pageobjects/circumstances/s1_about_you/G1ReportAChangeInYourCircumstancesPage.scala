package utils.pageobjects.circumstances.s1_about_you

import play.api.test.WithBrowser
import utils.pageobjects.{PageObjectsContext, CircumstancesPage, PageContext}

final class G1ReportAChangeInYourCircumstancesPage(ctx:PageObjectsContext) extends CircumstancesPage(ctx, G1ReportAChangeInYourCircumstancesPage.url) {
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
object G1ReportAChangeInYourCircumstancesPage {
  val url  = "/circumstances/identification/about-you"

  def apply(ctx:PageObjectsContext) = new G1ReportAChangeInYourCircumstancesPage(ctx)
}

/** The context for Specs tests */
trait G1ReportAChangeInYourCircumstancesPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G1ReportAChangeInYourCircumstancesPage(PageObjectsContext(browser))
}
