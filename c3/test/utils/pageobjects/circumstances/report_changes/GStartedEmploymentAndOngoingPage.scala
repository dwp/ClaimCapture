package utils.pageobjects.circumstances.report_changes

import utils.pageobjects.{PageContext, CircumstancesPage, PageObjectsContext}
import utils.WithBrowser

final class GStartedEmploymentAndOngoingPage(ctx:PageObjectsContext) extends CircumstancesPage(ctx, GStartedEmploymentAndOngoingPage.url) {
  declareYesNo("#beenPaidYet", "CircumstancesEmploymentChangeBeenPaidYet")
  declareInput("#howMuchPaid", "CircumstancesEmploymentChangeHowMuchPaid")
  declareDate("#whatDatePaid", "CircumstancesEmploymentChangeWhatDatePaid")
  declareSelect("#howOften_frequency", "CircumstancesEmploymentChangeHowOftenFrequency")
  declareInput("#howOften_frequency_other", "CircumstancesEmploymentChangeHowOftenFrequencyOther")
  declareInput("#monthlyPayDay", "CircumstancesEmploymentChangeMonthlyPayDay")
  declareYesNo("#usuallyPaidSameAmount", "CircumstancesEmploymentChangeUsuallyPaidSameAmount")
  declareYesNo("#doYouPayIntoPension_answer", "CircumstancesEmploymentChangeDoYouPayIntoPensionAnswer")
  declareInput("#doYouPayIntoPension_whatFor", "CircumstancesEmploymentChangeDoYouPayIntoPensionWhatFor")
  declareYesNo("#doYouPayForThings_answer", "CircumstancesEmploymentChangeDoYouPayForThingsAnswer")
  declareInput("#doYouPayForThings_whatFor", "CircumstancesEmploymentChangeDoYouPayForThingsWhatFor")
  declareYesNo("#doCareCostsForThisWork_answer", "CircumstancesEmploymentChangeDoCareCostsForThisWorkAnswer")
  declareInput("#doCareCostsForThisWork_whatCosts", "CircumstancesEmploymentChangeDoCareCostsForThisWorkWhatCosts")
  declareInput("#moreAboutChanges", "CircumstancesEmploymentChangeMoreAboutChanges")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object GStartedEmploymentAndOngoingPage {
  val url  = "/circumstances/report-changes/employment-ongoing"

  def apply(ctx:PageObjectsContext) = new GStartedEmploymentAndOngoingPage(ctx)
}

/** The context for Specs tests */
trait GStartedEmploymentAndOngoingPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GStartedEmploymentAndOngoingPage(PageObjectsContext(browser))
}
