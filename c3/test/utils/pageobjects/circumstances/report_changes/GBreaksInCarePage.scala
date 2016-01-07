package utils.pageobjects.circumstances.report_changes

import utils.pageobjects.{PageContext, CircumstancesPage, PageObjectsContext}
import utils.WithBrowser

/**
 * Created by neddakaltcheva on 3/20/14.
 */

final class GBreaksInCarePage(ctx:PageObjectsContext) extends CircumstancesPage(ctx, GBreaksInCarePage.url) {
    declareDate("#breaksInCareStartDate", "BreaksInCareStartDate")
    declareInput("#breaksInCareStartTime", "BreaksInCareStartTime")
    declareRadioList("#wherePersonBreaksInCare_answer", "BreaksInCareWhereWasThePersonYouCareFor")
    declareInput("#wherePersonBreaksInCare_text", "BreaksInCareWhereWasThePersonYouCareForSomewhereElse")
    declareRadioList("#whereYouBreaksInCare_answer", "BreaksInCareWhereWereYou")
    declareInput("#whereYouBreaksInCare_text", "BreaksInCareWhereWereYouSomewhereElse")
    declareYesNo("#breakEnded_answer", "BreaksInCareEnded")
    declareDate("#breakEnded_endDate", "BreaksInCareEndDate")
    declareInput("#breakEnded_endTime", "BreaksInCareEndTime")
    declareYesNoDontKnow("#expectStartCaring_answer", "BreaksInCareExpectToStartCaringAgain")
    declareDate("#expectStartCaring_expectStartCaringDate", "BreaksInCareExpectToStartCaringAgainDate")
    declareDate("#expectStartCaring_permanentBreakDate", "BreaksInCareExpectToStartCaringPermanentEndDate")
    declareYesNo("#medicalCareDuringBreak", "BreaksInCareMedicalCareDuringBreak")
    declareInput("#moreAboutChanges", "BreaksInCareMoreAboutChanges")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object GBreaksInCarePage {
  val url  = "/circumstances/report-changes/breaks-in-care"

  def apply(ctx:PageObjectsContext) = new GBreaksInCarePage(ctx)
}

/** The context for Specs tests */
trait GBreaksInCarePageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GBreaksInCarePage(PageObjectsContext(browser))
}

