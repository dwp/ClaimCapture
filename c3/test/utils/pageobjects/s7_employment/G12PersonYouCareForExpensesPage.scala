package utils.pageobjects.s7_employment

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

final class G12PersonYouCareForExpensesPage(browser: TestBrowser, previousPage: Option[Page] = None, iteration: Int) extends Page(browser, G12PersonYouCareForExpensesPage.url, G12PersonYouCareForExpensesPage.title, previousPage) {
  override val url = super.getUrl.replace(":jobID",iteration.toString)

    declareInput("#howMuchCostCare", "EmploymentCareExpensesHowMuchYouPayfor_"+iteration)
    declareInput("#whoDoYouPay", "EmploymentNameOfPersonYouPayForCaring_"+iteration)
    declareInput("#relationToYou", "EmploymentCareExpensesWhatRelationIsToYou_"+iteration)
    /* missing EmploymentCareExpensesWhatRelationIsToPersonYouCareFor */
  
}

object G12PersonYouCareForExpensesPage {
  val title = "Expenses related to the person you care for , while you are at work - Employment"
  val url  = "/employment/personYouCareForExpenses/:jobID"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None, iteration: Int) = new G12PersonYouCareForExpensesPage(browser,previousPage,iteration)
}

trait G12PersonYouCareForExpensesPageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = G12PersonYouCareForExpensesPage buildPageWith(browser,iteration = 1)
}
