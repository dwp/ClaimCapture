package utils.pageobjects.s8_self_employment

import play.api.test.WithBrowser
import utils.pageobjects._

final class G5ChildcareExpensesWhileAtWorkPage (ctx:PageObjectsContext) extends ClaimPage(ctx, G5ChildcareExpensesWhileAtWorkPage.url, G5ChildcareExpensesWhileAtWorkPage.title) {
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

  def apply(ctx:PageObjectsContext) = new G5ChildcareExpensesWhileAtWorkPage(ctx)
}

trait G5ChildcareExpensesWhileAtWorkPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G5ChildcareExpensesWhileAtWorkPage (PageObjectsContext(browser))
}