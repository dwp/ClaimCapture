package utils.pageobjects.s_employment

import utils.WithBrowser
import utils.pageobjects._

final class GPensionAndExpensesPage(ctx:PageObjectsContext, iteration: Int) extends ClaimPage(ctx, s"${GPensionAndExpensesPage.url}/${iteration.toString}", iteration) {
  declareYesNo("#payPensionScheme_answer", "EmploymentDoYouPayForPensionExpenses_" + iteration)
  declareInput("#payPensionScheme_text", "EmploymentPensionExpenses_" + iteration)
  declareYesNo("#payForThings_answer", "EmploymentDoYouPayForThingsToDoJob_" + iteration)
  declareInput("#payForThings_text", "EmploymentPayForThings_" + iteration)
  declareYesNo("#haveExpensesForJob_answer", "EmploymentDoYouPayforAnythingNecessaryToDoYourJob_" + iteration)
  declareInput("#haveExpensesForJob_text", "EmploymentWhatAreNecessaryJobExpenses_" + iteration)
}

object GPensionAndExpensesPage {
  val url  = "/employment/about-expenses"

  def apply(ctx:PageObjectsContext, iteration: Int= 1) = new GPensionAndExpensesPage(ctx,iteration)
}

trait GPensionAndExpensesPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GPensionAndExpensesPage (PageObjectsContext(browser),iteration = 1)
}
