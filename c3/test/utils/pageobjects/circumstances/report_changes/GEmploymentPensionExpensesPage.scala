package utils.pageobjects.circumstances.report_changes

import utils.pageobjects.{PageContext, CircumstancesPage, PageObjectsContext}
import utils.WithBrowser

final class GEmploymentPensionExpensesPage(ctx:PageObjectsContext) extends CircumstancesPage(ctx, GEmploymentPensionExpensesPage.url) {
  declareYesNo("#payIntoPension_answer", "CircumstancesEmploymentPayIntoPensionAnswer")
  declareInput("#payIntoPension_whatFor", "CircumstancesEmploymentPayIntoPensionWhatFor")
  declareYesNo("#payForThings_answer", "CircumstancesEmploymentPayForThingsAnswer")
  declareInput("#payForThings_whatFor", "CircumstancesEmploymentPayForThingsWhatFor")
  declareYesNo("#careCosts_answer", "CircumstancesEmploymentCareCostsAnswer")
  declareInput("#careCosts_whatFor", "CircumstancesEmploymentCareCostsWhatFor")
  declareInput("#moreAboutChanges", "CircumstancesEmploymentMoreAbout")
}

object GEmploymentPensionExpensesPage {
  val url  = "/circumstances/employment-pension-expenses"

  def apply(ctx:PageObjectsContext) = new GEmploymentPensionExpensesPage(ctx)
}

trait GEmploymentPensionExpensesPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GEmploymentPensionExpensesPage(PageObjectsContext(browser))
}
