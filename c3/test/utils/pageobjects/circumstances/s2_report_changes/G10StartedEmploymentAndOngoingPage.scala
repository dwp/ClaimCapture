package utils.pageobjects.circumstances.s2_report_changes

import utils.pageobjects.{PageContext, CircumstancesPage, PageObjectsContext}
import play.api.test.WithBrowser

final class G10StartedEmploymentAndOngoingPage(ctx:PageObjectsContext) extends CircumstancesPage(ctx, G10StartedEmploymentAndOngoingPage.url, G10StartedEmploymentAndOngoingPage.title) {
  declareYesNo("#beenPaidYet", "CircumstancesEmploymentChangeBeenPaidYet")
  declareInput("#howMuchPaid", "CircumstancesEmploymentChangeHowMuchPaid")
  declareDate("#whatDatePaid", "CircumstancesEmploymentChangeWhatDatePaid")
  declareSelect("#howOften_frequency", "CircumstancesEmploymentChangeHowOftenFrequency")
  declareInput("#howOften_frequency_other", "CircumstancesEmploymentChangeHowOftenFrequencyOther")
  declareInput("#monthlyPayDay", "CircumstancesEmploymentChangeMonthlyPayDay")
  declareYesNo("#usuallyPaidSameAmount", "CircumstancesEmploymentChangeUsuallyPaidSameAmount")
  declareYesNo("#doYouPayIntoPension_answer", "CircumstancesEmploymentChangeDoYouPayIntoPensionAnswer")
  declareInput("#doYouPayIntoPension_whatFor", "CircumstancesEmploymentChangeDoYouPayIntoPensionWhatFor")
  declareYesNo("#doCareCostsForThisWork_answer", "CircumstancesEmploymentChangeDoCareCostsForThisWorkAnswer")
  declareInput("#doCareCostsForThisWork_whatCosts", "CircumstancesEmploymentChangeDoCareCostsForThisWorkWhatCosts")
  declareInput("#moreAboutChanges", "CircumstancesEmploymentChangeMoreAboutChanges")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G10StartedEmploymentAndOngoingPage {
  val title = "Employment - Change in circumstances".toLowerCase

  val url  = "/circumstances/report-changes/employment-ongoing"

  def apply(ctx:PageObjectsContext) = new G10StartedEmploymentAndOngoingPage(ctx)
}

/** The context for Specs tests */
trait G10StartedEmploymentAndOngoingPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G10StartedEmploymentAndOngoingPage(PageObjectsContext(browser))
}