package utils.pageobjects.s7_employment

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

final class G5AdditionalWageDetailsPage(browser: TestBrowser, previousPage: Option[Page] = None, iteration:Int) extends Page(browser, G5AdditionalWageDetailsPage.url, G5AdditionalWageDetailsPage.title, previousPage) {
  override val url = super.getUrl.replace(":jobID",iteration.toString)

    declarePaymentFrequency("#oftenGetPaid","EmploymentAddtionalWageHowOftenAreYouPaid_"+iteration)
    declareInput("#whenGetPaid","EmploymentAddtionalWageWhenDoYouGetPaid_"+iteration)
    declareYesNo("#holidaySickPay","EmploymentAdditionalWageDoYouGetHolidayPayorSickPay_"+iteration)
    declareYesNo("#anyOtherMoney","EmploymentAddtionalWageDoYouGetPaidAnyOtherMoney_"+iteration)
    declareInput("#otherMoney","EmploymentAdditionalWageOtherMoneyYouReceived_"+iteration)
    declareYesNo("#employerOwesYouMoney","EmploymentAdditionalWageDoesYourEmployerOweYouAnyMoney_"+iteration)

}

object G5AdditionalWageDetailsPage {
  val title = "Additional wage details - Employment"
  val url  = "/employment/wageDetails"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None, iteration:Int) = new G5AdditionalWageDetailsPage(browser,previousPage, iteration)
}

trait G5AdditionalWageDetailsPageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = G5AdditionalWageDetailsPage buildPageWith (browser, iteration = 1)
}
