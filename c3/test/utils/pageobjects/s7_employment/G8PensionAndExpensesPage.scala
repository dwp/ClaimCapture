package utils.pageobjects.s7_employment

import utils.WithBrowser
import utils.pageobjects._

final class G8PensionAndExpensesPage(ctx:PageObjectsContext, iteration: Int) extends ClaimPage(ctx, s"${G8PensionAndExpensesPage.url}/${iteration.toString}", iteration) {
  declareYesNo("#payPensionScheme_answer", "EmploymentDoYouPayForPensionExpenses_" + iteration)
  declareInput("#payPensionScheme_text", "EmploymentPensionExpenses_" + iteration)
  declareYesNo("#payForThings_answer", "EmploymentDoYouPayForThingsToDoJob_" + iteration)
  declareInput("#payForThings_text", "EmploymentPayForThings_" + iteration)
  declareYesNo("#haveExpensesForJob_answer", "EmploymentDoYouPayforAnythingNecessaryToDoYourJob_" + iteration)
  declareInput("#haveExpensesForJob_text", "EmploymentWhatAreNecessaryJobExpenses_" + iteration)
}

object G8PensionAndExpensesPage {
  val url  = "/employment/about-expenses"

  def apply(ctx:PageObjectsContext, iteration: Int= 1) = new G8PensionAndExpensesPage(ctx,iteration)
}

trait G8PensionAndExpensesPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G8PensionAndExpensesPage (PageObjectsContext(browser),iteration = 1)
}