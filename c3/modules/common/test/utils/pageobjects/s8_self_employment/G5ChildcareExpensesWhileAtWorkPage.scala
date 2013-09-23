package utils.pageobjects.s8_self_employment

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{ClaimPage,Page, PageContext}

final class G5ChildcareExpensesWhileAtWorkPage (browser: TestBrowser, previousPage: Option[Page] = None) extends ClaimPage(browser, G5ChildcareExpensesWhileAtWorkPage.url, G5ChildcareExpensesWhileAtWorkPage.title, previousPage) {
  declareInput("#whoLooksAfterChildren", "SelfEmployedChildcareProviderNameOfPerson")
  declareInput("#howMuchYouPay", "SelfEmployedChildcareExpensesHowMuchYouPay")
  declareSelect("#howOftenPayChildCare_frequency","SelfEmployedChildcareExpensesHowOften")
  declareInput("#howOftenPayChildCare_frequency_other","SelfEmployedChildcareExpensesHowOftenOther")
  declareSelect("#whatRelationIsToYou", "SelfEmployedChildcareProviderWhatRelationIsToYou")
  declareSelect("#relationToPartner", "SelfEmployedChildcareProviderWhatRelationIsToYourPartner")
  declareSelect("#whatRelationIsTothePersonYouCareFor", "SelfEmployedChildcareProviderWhatRelationIsTothePersonYouCareFor")
}

object G5ChildcareExpensesWhileAtWorkPage {
  val title = "Childcare expenses while you are at work - About self-employment".toLowerCase
  val url = "/self-employment/childcare-expenses-while-at-work"

  def apply(browser: TestBrowser, previousPage: Option[Page] = None) = new G5ChildcareExpensesWhileAtWorkPage(browser, previousPage)
}

trait G5ChildcareExpensesWhileAtWorkPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G5ChildcareExpensesWhileAtWorkPage (browser)
}