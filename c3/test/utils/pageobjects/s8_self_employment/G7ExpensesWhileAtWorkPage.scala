package utils.pageobjects.s8_self_employment

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{PageContext, Page}

final class G7ExpensesWhileAtWorkPage(browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G7ExpensesWhileAtWorkPage.url, G7ExpensesWhileAtWorkPage.title, previousPage) {
  declareInput("#howMuchYouPay", "SelfEmployedCareExpensesHowMuchYouPay")
  declareInput("#nameOfPerson", "SelfEmployedCareExpensesNameOfPerson")
  declareSelect("#whatRelationIsToYou", "SelfEmployedCareExpensesWhatRelationIsToYou")
  declareInput("#whatRelationIsTothePersonYouCareFor", "SelfEmployedCareExpensesWhatRelationIsTothePersonYouCareFor")
}

object G7ExpensesWhileAtWorkPage {
  val title = "Expenses related to the person you care for while at work - About self-employment"

  val url = "/self-employment/expenses-while-at-work"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G7ExpensesWhileAtWorkPage(browser, previousPage)
}

trait G7ExpensesWhileAtWorkPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G7ExpensesWhileAtWorkPage buildPageWith browser
}