package utils.pageobjects.s7_employment

import play.api.test.WithBrowser
import utils.pageobjects._

final class G8AboutExpensesPage(ctx:PageObjectsContext, iteration: Int) extends ClaimPage(ctx, G8AboutExpensesPage.url.replace(":jobID", iteration.toString), G8AboutExpensesPage.title, iteration) {
  declareYesNo("#haveExpensesForJob", "EmploymentDoYouPayforAnythingNecessaryToDoYourJob_" + iteration)
  declareInput("#jobTitle", "EmploymentJobTitle_" + iteration)
  declareInput("#whatExpensesForJob", "EmploymentWhatAreNecessaryJobExpenses_" + iteration)
  declareYesNo("#payAnyoneToLookAfterChildren", "EmploymentDoYouPayAnyoneLookAfterYourChild_" + iteration)
  declareInput("#nameLookAfterChildren", "EmploymentNameOfthePersonWhoLooksAfterYourChild_" + iteration)
  declareInput("#howMuchLookAfterChildren", "EmploymentChildcareExpensesHowMuchYouPayfor_" + iteration)
  declareSelect("#howOftenLookAfterChildren_frequency","EmploymentChildcareExpensesHowOften_" + iteration)
  declareInput("#howOftenLookAfterChildren_frequency_other","EmploymentChildcareExpensesHowOftenOther_" + iteration)
  declareInput("#relationToYouLookAfterChildren", "EmploymentChildcareExpensesWhatRelationIsthePersontoYou_" + iteration)
  declareInput("#relationToPersonLookAfterChildren", "EmploymentChildcareExpensesWhatRelationIsthePersonToThePersonYouCareFor_" + iteration)
  declareYesNo("#payAnyoneToLookAfterPerson", "EmploymentDoYouPayAnyonetoLookAfterPersonYouCareFor_" + iteration)
  declareInput("#nameLookAfterPerson", "EmploymentNameOfPersonYouPayForCaring_" + iteration)
  declareInput("#howMuchLookAfterPerson", "EmploymentCareExpensesHowMuchYouPayfor_" + iteration)
  declareSelect("#howOftenLookAfterPerson_frequency", "EmploymentCareExpensesHowOftenYouPayfor_" + iteration)
  declareInput("#howOftenLookAfterPerson_frequency_other","EmploymentCareExpensesHowOftenYouPayforOther_" + iteration)
  declareInput("#relationToYouLookAfterPerson", "EmploymentCareExpensesWhatRelationIsToYou_" + iteration)
  declareInput("#relationToPersonLookAfterPerson", "EmploymentCareExpensesWhatRelationIsToPersonYouCareFor_" + iteration)
}

object G8AboutExpensesPage {
  val title = "Your expenses - Employment History".toLowerCase

  val url  = "/employment/about-expenses/:jobID"

  def apply(ctx:PageObjectsContext, iteration: Int= 1) = new G8AboutExpensesPage(ctx,iteration)
}

trait G8AboutExpensesPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G8AboutExpensesPage (PageObjectsContext(browser),iteration = 1)
}