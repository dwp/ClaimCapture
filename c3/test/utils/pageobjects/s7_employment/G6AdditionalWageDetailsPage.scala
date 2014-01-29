package utils.pageobjects.s7_employment

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{ClaimPage,Page, PageContext}

final class G6AdditionalWageDetailsPage(browser: TestBrowser, previousPage: Option[Page] = None, iteration: Int) extends ClaimPage(browser, G6AdditionalWageDetailsPage.url.replace(":jobID", iteration.toString), G6AdditionalWageDetailsPage.title, previousPage, iteration) {
  declareSelect("#oftenGetPaid_frequency","EmploymentAddtionalWageHowOftenAreYouPaid_" + iteration)
  declareInput("#oftenGetPaid_frequency_other","EmploymentAddtionalWageOther_" + iteration)
  declareInput("#whenGetPaid","EmploymentAddtionalWageWhenDoYouGetPaid_" + iteration)
  declareYesNo("#employerOwesYouMoney","EmploymentAdditionalWageDoesYourEmployerOweYouAnyMoney_" + iteration)
}

object G6AdditionalWageDetailsPage {
  val title = "Additional details on your last wage - Employment History".toLowerCase

  val url  = "/employment/additional-wage-details/:jobID"

  def apply(browser: TestBrowser, previousPage: Option[Page] = None, iteration: Int) = new G6AdditionalWageDetailsPage(browser,previousPage, iteration)
}

trait G6AdditionalWageDetailsPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G6AdditionalWageDetailsPage (browser, iteration = 1)
}