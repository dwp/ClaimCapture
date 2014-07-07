package utils.pageobjects.s7_employment

import play.api.test.WithBrowser
import utils.pageobjects._

final class G5LastWagePage(ctx:PageObjectsContext, iteration:Int) extends ClaimPage(ctx, G5LastWagePage.url.replace(":jobID",iteration.toString), G5LastWagePage.title,iteration) {
  declareSelect("#oftenGetPaid_frequency","EmploymentAddtionalWageHowOftenAreYouPaid_" + iteration)
  declareInput("#oftenGetPaid_frequency_other","EmploymentAddtionalWageOther_" + iteration)
  declareInput("#whenGetPaid","EmploymentAddtionalWageWhenDoYouGetPaid_" + iteration)
  declareDate("#lastPaidDate", "EmploymentWhenWereYouLastPaid_" + iteration)
  declareInput("#grossPay", "EmploymentWhatWasTheGrossPayForTheLastPayPeriod_" + iteration)
  declareInput("#payInclusions", "EmploymentWhatWasIncludedInYourLastPay_" + iteration)
  declareInput("#sameAmountEachTime", "EmploymentDoYouGettheSameAmountEachTime_" + iteration)
  declareYesNo("#employerOwesYouMoney","EmploymentAdditionalWageDoesYourEmployerOweYouAnyMoney_" + iteration)
}

object G5LastWagePage {
  val title = "Your wages - Employment History".toLowerCase

  val url  = "/employment/last-wage/:jobID"

  def apply(ctx:PageObjectsContext, iteration:Int=1) = new G5LastWagePage(ctx,iteration)
}

trait G5LastWagePageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G5LastWagePage (PageObjectsContext(browser),iteration = 1)
}