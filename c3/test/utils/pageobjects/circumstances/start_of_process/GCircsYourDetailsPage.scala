package utils.pageobjects.circumstances.start_of_process

import utils.WithBrowser
import utils.pageobjects.{PageObjectsContext, CircumstancesPage, PageContext}

final class GCircsYourDetailsPage(ctx:PageObjectsContext) extends CircumstancesPage(ctx, GCircsYourDetailsPage.url) {
  declareInput("#fullName","CircumstancesAboutYouFullName")
  declareNino("#nationalInsuranceNumber","CircumstancesAboutYouNationalInsuranceNumber")
  declareDate("#dateOfBirth", "CircumstancesAboutYouDateOfBirth")
  declareInput("#theirFullName","CircumstancesAboutYouTheirFullName")
  declareInput("#theirRelationshipToYou","CircumstancesAboutYouTheirRelationshipToYou")
  declareInput("#furtherInfoContact","FurtherInfoContact")
  declareYesNo("#wantsEmailContactCircs","CircumstancesDeclarationWantsEmailContact")
  declareInput("#mail","CircumstancesDeclarationMail")
  declareInput("#mailConfirmation","CircumstancesDeclarationMailConfirmation")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object GCircsYourDetailsPage {
  val url  = "/circumstances/identification/about-you"

  def apply(ctx:PageObjectsContext) = new GCircsYourDetailsPage(ctx)
}

/** The context for Specs tests */
trait GCircsYourDetailsPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GCircsYourDetailsPage(PageObjectsContext(browser))
}
