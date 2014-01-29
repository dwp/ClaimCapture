package utils.pageobjects.s7_employment

import play.api.test.WithBrowser
import utils.pageobjects._

final class G6AdditionalWageDetailsPage(ctx:PageObjectsContext, iteration: Int) extends ClaimPage(ctx, G6AdditionalWageDetailsPage.url.replace(":jobID", iteration.toString), G6AdditionalWageDetailsPage.title, iteration) {
  declareSelect("#oftenGetPaid_frequency","EmploymentAddtionalWageHowOftenAreYouPaid_" + iteration)
  declareInput("#oftenGetPaid_frequency_other","EmploymentAddtionalWageOther_" + iteration)
  declareInput("#whenGetPaid","EmploymentAddtionalWageWhenDoYouGetPaid_" + iteration)
  declareYesNo("#employerOwesYouMoney","EmploymentAdditionalWageDoesYourEmployerOweYouAnyMoney_" + iteration)
}

object G6AdditionalWageDetailsPage {
  val title = "Additional details on your last wage - Employment History".toLowerCase

  val url  = "/employment/additional-wage-details/:jobID"

  def apply(ctx:PageObjectsContext, iteration: Int = 1) = new G6AdditionalWageDetailsPage(ctx, iteration)
}

trait G6AdditionalWageDetailsPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G6AdditionalWageDetailsPage (PageObjectsContext(browser), iteration = 1)
}