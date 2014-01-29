package utils.pageobjects.s7_employment

import play.api.test.WithBrowser
import utils.pageobjects._

final class G9NecessaryExpensesPage(ctx:PageObjectsContext, iteration: Int) extends ClaimPage(ctx, G9NecessaryExpensesPage.url.replace(":jobID", iteration.toString), G9NecessaryExpensesPage.title, iteration) {
  declareInput("#jobTitle", "EmploymentJobTitle_" + iteration)
  declareInput("#whatAreThose", "EmploymentWhatAreNecessaryJobExpenses_" + iteration)
}

object G9NecessaryExpensesPage {
  val title = "Necessary expenses to do your job - Employment History".toLowerCase

  val url  = "/employment/necessary-expenses/:jobID"

  def apply(ctx:PageObjectsContext, iteration: Int= 1) = new G9NecessaryExpensesPage(ctx, iteration)
}

trait G9NecessaryExpensesPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G9NecessaryExpensesPage (PageObjectsContext(browser), iteration = 1)
}