package utils.pageobjects.s8_self_employment

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{PageContext, Page}

final class G5ChildcareExpensesWhileAtWorkPage (browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G5ChildcareExpensesWhileAtWorkPage.url, G5ChildcareExpensesWhileAtWorkPage.title, previousPage) {
  declareInput("#howMuchYouPay", "SelfEmployedChildcareExpensesHowMuchYouPay")
  declareInput("#whoLooksAfterChildren", "SelfEmployedChildcareProviderNameOfPerson")
  declareSelect("#whatRelationIsToYou", "SelfEmployedChildcareProviderWhatRelationIsToYou")
  declareSelect("#relationToPartner", "SelfEmployedChildcareProviderWhatRelationIsToYourPartner")
  declareInput("#whatRelationIsTothePersonYouCareFor", "SelfEmployedChildcareProviderWhatRelationIsTothePersonYouCareFor")
}

object G5ChildcareExpensesWhileAtWorkPage {
  val title = "Childcare expenses while you are at work - About self-employment"

  val url = "/self-employment/childcare-expenses-while-at-work"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G5ChildcareExpensesWhileAtWorkPage(browser, previousPage)
}

trait G5ChildcareExpensesWhileAtWorkPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G5ChildcareExpensesWhileAtWorkPage buildPageWith browser
}