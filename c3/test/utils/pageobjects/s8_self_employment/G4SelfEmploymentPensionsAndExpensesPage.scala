package utils.pageobjects.s8_self_employment

import play.api.test.WithBrowser
import utils.pageobjects._

final class G4SelfEmploymentPensionsAndExpensesPage (ctx:PageObjectsContext) extends ClaimPage(ctx, G4SelfEmploymentPensionsAndExpensesPage.url) {
  declareYesNo("#payPensionScheme_answer", "SelfEmploymentDoYouPayForPensionExpenses")
  declareInput("#payPensionScheme_text", "SelfEmploymentPensionExpenses")
  declareYesNo("#haveExpensesForJob_answer", "SelfEmploymentDoYouPayForAnythingNecessaryToDoYourJob")
  declareInput("#haveExpensesForJob_text", "SelfEmploymentWhatAreNecessaryJobExpenses")
}

object G4SelfEmploymentPensionsAndExpensesPage {
  val url = "/self-employment/pensions-and-expenses"

  def apply(ctx:PageObjectsContext) = new G4SelfEmploymentPensionsAndExpensesPage(ctx)
}

trait G4SelfEmploymentPensionsAndExpensesPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G4SelfEmploymentPensionsAndExpensesPage (PageObjectsContext(browser))
}