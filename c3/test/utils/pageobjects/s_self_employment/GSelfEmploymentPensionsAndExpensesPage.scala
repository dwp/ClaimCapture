package utils.pageobjects.s_self_employment

import utils.WithBrowser
import utils.pageobjects._

final class GSelfEmploymentPensionsAndExpensesPage (ctx:PageObjectsContext) extends ClaimPage(ctx, GSelfEmploymentPensionsAndExpensesPage.url) {
  declareYesNo("#payPensionScheme_answer", "SelfEmploymentDoYouPayForPensionExpenses")
  declareInput("#payPensionScheme_text", "SelfEmploymentPensionExpenses")
  declareYesNo("#haveExpensesForJob_answer", "SelfEmploymentDoYouPayForAnythingNecessaryToDoYourJob")
  declareInput("#haveExpensesForJob_text", "SelfEmploymentWhatAreNecessaryJobExpenses")
}

object GSelfEmploymentPensionsAndExpensesPage {
  val url = "/self-employment/pensions-and-expenses"

  def apply(ctx:PageObjectsContext) = new GSelfEmploymentPensionsAndExpensesPage(ctx)
}

trait GSelfEmploymentPensionsAndExpensesPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GSelfEmploymentPensionsAndExpensesPage (PageObjectsContext(browser))
}
