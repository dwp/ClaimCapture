package utils.pageobjects.s7_employment

import play.api.test.WithBrowser
import utils.pageobjects._

final class G5AdditionalWageDetailsPage(ctx:PageObjectsContext, iteration: Int) extends ClaimPage(ctx, G5AdditionalWageDetailsPage.url.replace(":jobID", iteration.toString), G5AdditionalWageDetailsPage.title, iteration) {
  declareSelect("#oftenGetPaid_frequency","EmploymentAddtionalWageHowOftenAreYouPaid_" + iteration)
  declareInput("#oftenGetPaid_frequency_other","EmploymentAddtionalWageOther_" + iteration)
  declareInput("#whenGetPaid","EmploymentAddtionalWageWhenDoYouGetPaid_" + iteration)
  declareYesNo("#employerOwesYouMoney","EmploymentAdditionalWageDoesYourEmployerOweYouAnyMoney_" + iteration)
}

object G5AdditionalWageDetailsPage {
  val title = "Additional details on your last wage - Employment History".toLowerCase

  val url  = "/employment/additional-wage-details/:jobID"

  def apply(ctx:PageObjectsContext, iteration: Int) = new G5AdditionalWageDetailsPage(ctx, iteration)
}

trait G5AdditionalWageDetailsPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G5AdditionalWageDetailsPage (PageObjectsContext(browser), iteration = 1)
}