package utils.pageobjects.s8_self_employment

import play.api.test.TestBrowser
import utils.pageobjects.{PageContext, Page}

final class G7ExpensesWhileAtWorkPage(browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G7ExpensesWhileAtWorkPage.url, G7ExpensesWhileAtWorkPage.title, previousPage) {

    declareInput("#howMuchYouPay", "SelfEmployedCareExpensesHowMuchYouPay")
    declareInput("#nameOfPerson", "SelfEmployedCareExpensesNameOfPerson")
    declareSelect("#whatRelationIsToYou", "SelfEmployedCareExpensesWhatRelationIsToYou")
    declareInput("#whatRelationIsTothePersonYouCareFor", "SelfEmployedCareExpensesWhatRelationIsTothePersonYouCareFor")

}

object G7ExpensesWhileAtWorkPage {
  val title = "Expenses related to the person you care for while at work - Self Employment"
  val url = "/selfEmployment/expensesWhileAtWork"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G7ExpensesWhileAtWorkPage(browser, previousPage)
}

trait G7ExpensesWhileAtWorkPageContext extends PageContext {
  this: {val browser: TestBrowser} =>
  val page = G7ExpensesWhileAtWorkPage buildPageWith browser
}
