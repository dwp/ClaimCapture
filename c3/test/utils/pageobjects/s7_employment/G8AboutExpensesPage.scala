package utils.pageobjects.s7_employment

import play.api.test.WithBrowser
import utils.pageobjects._

final class G8AboutExpensesPage(ctx:PageObjectsContext, iteration: Int) extends ClaimPage(ctx, G8AboutExpensesPage.url.replace(":jobID", iteration.toString), G8AboutExpensesPage.title, iteration) {
  declareYesNo("#payForAnythingNecessary", "EmploymentDoYouPayforAnythingNecessaryToDoYourJob_" + iteration)
  declareYesNo("#payAnyoneToLookAfterChildren", "EmploymentDoYouPayAnyoneLookAfterYourChild_" + iteration)
  declareYesNo("#payAnyoneToLookAfterPerson", "EmploymentDoYouPayAnyonetoLookAfterPersonYouCareFor_" + iteration)
}

object G8AboutExpensesPage {
  val title = "About expenses to do with your employment - Employment History".toLowerCase

  val url  = "/employment/about-expenses/:jobID"

  def apply(ctx:PageObjectsContext, iteration: Int= 1) = new G8AboutExpensesPage(ctx,iteration)
}

trait G8AboutExpensesPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G8AboutExpensesPage (PageObjectsContext(browser),iteration = 1)
}