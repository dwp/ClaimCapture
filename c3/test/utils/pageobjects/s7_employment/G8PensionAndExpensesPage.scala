package utils.pageobjects.s7_employment

import play.api.test.WithBrowser
import utils.pageobjects._

final class G8PensionAndExpensesPage(ctx:PageObjectsContext, iteration: Int) extends ClaimPage(ctx, G8PensionAndExpensesPage.url.replace(":jobID", iteration.toString), G8PensionAndExpensesPage.title, iteration) {
  declareYesNo("#payPensionScheme_answer", "EmploymentDoYouPayForPensionExpenses_" + iteration)
  declareInput("#payPensionScheme_text", "EmploymentPensionExpenses_" + iteration)
  declareYesNo("#payForThings_answer", "EmploymentDoYouPayForThingsToDoJob_" + iteration)
  declareInput("#payForThings_text", "EmploymentPayForThings_" + iteration)
  declareYesNo("#haveExpensesForJob_answer", "EmploymentDoYouPayforAnythingNecessaryToDoYourJob_" + iteration)
  declareInput("#haveExpensesForJob_text", "EmploymentWhatAreNecessaryJobExpenses_" + iteration)
}

object G8PensionAndExpensesPage {
  val title = "Pension and expenses - Employment History".toLowerCase

  val url  = "/employment/about-expenses/:jobID"

  def apply(ctx:PageObjectsContext, iteration: Int= 1) = new G8PensionAndExpensesPage(ctx,iteration)
}

trait G8PensionAndExpensesPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G8PensionAndExpensesPage (PageObjectsContext(browser),iteration = 1)
}