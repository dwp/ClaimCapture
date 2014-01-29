package utils.pageobjects.s7_employment

import play.api.test.WithBrowser
import utils.pageobjects._

final class G10ChildcareExpensesPage(ctx:PageObjectsContext, iteration: Int) extends ClaimPage(ctx, G10ChildcareExpensesPage.url.replace(":jobID", iteration.toString), G10ChildcareExpensesPage.title, iteration) {
  declareInput("#howMuchCostChildcare", "EmploymentChildcareExpensesHowMuchYouPayfor_" + iteration)
  declareSelect("#howOftenPayChildCare_frequency","EmploymentChildcareExpensesHowOften_" + iteration)
  declareInput("#howOftenPayChildCare_frequency_other","EmploymentChildcareExpensesHowOftenOther_" + iteration)
  declareInput("#whoLooksAfterChildren", "EmploymentNameOfthePersonWhoLooksAfterYourChild_" + iteration)
  declareSelect("#relationToYou", "EmploymentChildcareExpensesWhatRelationIsthePersontoYou_" + iteration)
  declareSelect("#relationToPartner", "EmploymentChildcareExpensesWhatRelationIsthePersontoYourPartner_" + iteration)
  declareSelect("#relationToPersonYouCare", "EmploymentChildcareExpensesWhatRelationIsthePersonToThePersonYouCareFor_" + iteration)
}

object G10ChildcareExpensesPage {
  val title = "Childcare expenses while you are at work - Employment History".toLowerCase

  val url  = "/employment/childcare-expenses/:jobID"

  def apply(ctx:PageObjectsContext, iteration: Int) = new G10ChildcareExpensesPage(ctx,iteration)
}

trait G10ChildcareExpensesPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G10ChildcareExpensesPage (PageObjectsContext(browser),iteration = 1)
}