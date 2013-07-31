package utils.pageobjects.s7_employment

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

final class G5AdditionalWageDetailsPage(browser: TestBrowser, previousPage: Option[Page] = None, iteration:Int) extends Page(browser, G5AdditionalWageDetailsPage.url, G5AdditionalWageDetailsPage.title, previousPage) {
  override val url = super.getUrl.replace(":jobID",iteration.toString)
  def fillPageWith(theClaim: ClaimScenario) {

    fillPaymentFrequency("#oftenGetPaid",theClaim.selectDynamic("EmploymentAddtionalWageHowOftenAreYouPaid_"+iteration))
    fillInput("#whenGetPaid",theClaim.selectDynamic("EmploymentAddtionalWageWhenDoYouGetPaid_"+iteration))
    fillYesNo("#holidaySickPay",theClaim.selectDynamic("EmploymentAdditionalWageDoYouGetHolidayPayorSickPay_"+iteration))
    fillYesNo("#anyOtherMoney",theClaim.selectDynamic("EmploymentAddtionalWageDoYouGetPaidAnyOtherMoney_"+iteration))
    fillInput("#otherMoney",theClaim.selectDynamic("EmploymentAdditionalWageOtherMoneyYouReceived_"+iteration))
    fillYesNo("#employeeOwesYouMoney",theClaim.selectDynamic("EmploymentAdditionalWageDoesYourEmployerOweYouAnyMoney_"+iteration))
  }
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
