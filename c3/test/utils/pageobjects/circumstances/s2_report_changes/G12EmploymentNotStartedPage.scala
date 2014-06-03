package utils.pageobjects.circumstances.s2_report_changes

import utils.pageobjects.{CircumstancesPage, PageContext, PageObjectsContext}
import play.api.test.WithBrowser

class G12EmploymentNotStartedPage(ctx:PageObjectsContext) extends CircumstancesPage(ctx, G12EmploymentNotStartedPage.url, G12EmploymentNotStartedPage.title) {
  declareYesNo("#beenPaidYet", "CircumstancesEmploymentChangeBeenPaidYet")
  declareInput("#howMuchPaid", "CircumstancesEmploymentChangeHowMuchPaid")
  declareDate("#whenExpectedToBePaidDate", "CircumstancesEmploymentChangeWhatDatePaid")
  declareSelect("#howOften_frequency", "CircumstancesEmploymentChangeHowOftenFrequency")
  declareInput("#howOften_frequency_other", "CircumstancesEmploymentChangeHowOftenFrequencyOther")
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
object G12EmploymentNotStartedPage {
  val title = "Future Employment - Change in circumstances".toLowerCase

  val url  = "/circumstances/report-changes/future-employment"

  def apply(ctx:PageObjectsContext) = new G12EmploymentNotStartedPage(ctx)
}

/** The context for Specs tests */
trait G12EmploymentNotStartedPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G12EmploymentNotStartedPage(PageObjectsContext(browser))
}