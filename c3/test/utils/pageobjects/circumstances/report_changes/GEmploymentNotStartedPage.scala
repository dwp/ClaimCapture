package utils.pageobjects.circumstances.report_changes

import utils.pageobjects.{CircumstancesPage, PageContext, PageObjectsContext}
import utils.WithBrowser

class GEmploymentNotStartedPage(ctx:PageObjectsContext) extends CircumstancesPage(ctx, GEmploymentNotStartedPage.url) {
  declareYesNo("#beenPaidYet", "CircumstancesEmploymentChangeBeenPaidYet")
  declareInput("#howMuchPaid", "CircumstancesEmploymentChangeHowMuchPaid")
  declareDate("#whenExpectedToBePaidDate", "CircumstancesEmploymentChangeWhatDatePaid")
  declareSelect("#howOften_frequency", "CircumstancesEmploymentChangeHowOftenFrequency")
  declareInput("#howOften_frequency_other", "CircumstancesEmploymentChangeHowOftenFrequencyOther")
  declareYesNo("#usuallyPaidSameAmount", "CircumstancesEmploymentChangeUsuallyPaidSameAmount")
  declareYesNo("#willYouPayIntoPension_answer", "CircumstancesEmploymentChangeWillYouPayIntoPensionAnswer")
  declareInput("#willYouPayIntoPension_whatFor", "CircumstancesEmploymentChangeWillYouPayIntoPensionWhatFor")
  declareYesNo("#willYouPayForThings_answer", "CircumstancesEmploymentChangeWillYouPayForThingsAnswer")
  declareInput("#willYouPayForThings_whatFor", "CircumstancesEmploymentChangeWillYouPayForThingsWhatFor")
  declareYesNo("#willCareCostsForThisWork_answer", "CircumstancesEmploymentChangeWillCareCostsForThisWorkAnswer")
  declareInput("#willCareCostsForThisWork_whatCosts", "CircumstancesEmploymentChangeWillCareCostsForThisWorkWhatCosts")
  declareInput("#moreAboutChanges", "CircumstancesEmploymentChangeMoreAboutChanges")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object GEmploymentNotStartedPage {
  val url  = "/circumstances/report-changes/future-employment"

  def apply(ctx:PageObjectsContext) = new GEmploymentNotStartedPage(ctx)
}

/** The context for Specs tests */
trait GEmploymentNotStartedPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GEmploymentNotStartedPage(PageObjectsContext(browser))
}
