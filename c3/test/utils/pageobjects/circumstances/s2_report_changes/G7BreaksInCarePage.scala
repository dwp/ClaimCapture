package utils.pageobjects.circumstances.s2_report_changes

import utils.pageobjects.{PageContext, CircumstancesPage, PageObjectsContext}
import play.api.test.WithBrowser

/**
 * Created by neddakaltcheva on 3/20/14.
 */

final class G7BreaksInCarePage(ctx:PageObjectsContext) extends CircumstancesPage(ctx, G7BreaksInCarePage.url, G7BreaksInCarePage.title) {
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
object G7BreaksInCarePage {
  val title = "Breaks from caring - change in circumstances".toLowerCase

  val url  = "/circumstances/report-changes/breaks-in-care"

  def apply(ctx:PageObjectsContext) = new G7BreaksInCarePage(ctx)
}

/** The context for Specs tests */
trait G7BreaksInCarePageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G7BreaksInCarePage(PageObjectsContext(browser))
}

