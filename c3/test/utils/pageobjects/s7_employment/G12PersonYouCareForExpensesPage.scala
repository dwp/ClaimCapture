package utils.pageobjects.s7_employment

import play.api.test.WithBrowser
import utils.pageobjects._

final class G12PersonYouCareForExpensesPage(ctx:PageObjectsContext, iteration: Int) extends ClaimPage(ctx, G12PersonYouCareForExpensesPage.url.replace(":jobID", iteration.toString), G12PersonYouCareForExpensesPage.title, iteration) {
  declareInput("#whoDoYouPay", "EmploymentNameOfPersonYouPayForCaring_" + iteration)
  declareInput("#howMuchCostCare", "EmploymentCareExpensesHowMuchYouPayfor_" + iteration)
  declareSelect("#howOftenPayCare_frequency", "EmploymentCareExpensesHowOftenYouPayfor_" + iteration)
  declareInput("#howOftenPayCare_frequency_other","EmploymentCareExpensesHowOftenYouPayforOther_" + iteration)
  declareSelect("#relationToYou", "EmploymentCareExpensesWhatRelationIsToYou_" + iteration)
  declareSelect("#relationToPersonYouCare", "EmploymentCareExpensesWhatRelationIsToPersonYouCareFor_" + iteration)
}

object G12PersonYouCareForExpensesPage {
  val title = "Expenses related to the person you care for, while you are at work - Employment History".toLowerCase

  val url  = "/employment/person-you-care-for-expenses/:jobID"

  def apply(ctx:PageObjectsContext, iteration: Int) = new G12PersonYouCareForExpensesPage(ctx,iteration)
}

trait G12PersonYouCareForExpensesPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G12PersonYouCareForExpensesPage (PageObjectsContext(browser),iteration = 1)
}