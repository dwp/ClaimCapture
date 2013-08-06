package utils.pageobjects.s7_employment

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{PageContext, Page}

final class G10ChildcareExpensesPage(browser: TestBrowser, previousPage: Option[Page] = None, iteration: Int)
  extends Page(browser, G10ChildcareExpensesPage.url, G10ChildcareExpensesPage.title, previousPage) {

  override val url = super.getUrl.replace(":jobID",iteration.toString)

  declareInput("#howMuchCostChildcare", "EmploymentChildcareExpensesHowMuchYouPayfor_"+iteration)
  declareInput("#whoLooksAfterChildren", "EmploymentNameOfthePersonWhoLooksAfterYourChild_"+iteration)
  declareInput("#relationToYou", "EmploymentChildcareExpensesWhatRelationIsthePersontoYou_"+iteration)
  declareInput("#relationToPartner", "EmploymentChildcareExpensesWhatRelationIsthePersontoYourPartner_"+iteration)
  declareInput("#relationToPersonYouCare", "EmploymentChildcareExpensesWhatRelationIsthePersonToThePersonYouCareFor_"+iteration)
}

object G10ChildcareExpensesPage {
  val title = "Childcare expenses while you are at work - Employment"

  val url  = "/employment/childcareExpenses/:jobID"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None, iteration: Int) = new G10ChildcareExpensesPage(browser,previousPage,iteration)
}

trait G10ChildcareExpensesPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G10ChildcareExpensesPage buildPageWith(browser, iteration = 1)
}