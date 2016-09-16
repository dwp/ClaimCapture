package utils.pageobjects.your_income

import controllers.ClaimScenarioFactory._
import utils.WithBrowser
import utils.pageobjects.{TestData, PageContext, ClaimPage, PageObjectsContext}

/**
  * Created by peterwhitehead on 29/03/2016.
  */
class GYourIncomePage(ctx:PageObjectsContext) extends ClaimPage(ctx, GYourIncomePage.url) {
  declareYesNo("#beenEmployedSince6MonthsBeforeClaim", "EmploymentHaveYouBeenEmployedAtAnyTime_0")
  declareYesNo("#beenSelfEmployedSince1WeekBeforeClaim", "EmploymentHaveYouBeenSelfEmployedAtAnyTime")
  declareCheck("#yourIncome_sickpay", "YourIncomeStatutorySickPay")
  declareCheck("#yourIncome_patmatadoppay", "YourIncomePatMatAdopPay")
  declareCheck("#yourIncome_fostering", "YourIncomeFosteringAllowance")
  declareCheck("#yourIncome_directpay", "YourIncomeDirectPay")
  declareCheck("#yourIncome_rental", "YourIncomeRentalIncome")
  declareCheck("#yourIncome_anyother", "YourIncomeAnyOtherPay")
  declareCheck("#yourIncome_none", "YourIncomeNone")
}

object GYourIncomePage {
  val url  = "/your-income/your-income"

  def apply(ctx:PageObjectsContext) = new GYourIncomePage(ctx)

  def fillYourIncomes(context: PageObjectsContext, f: => TestData => Unit) = {
    val claimData = s7NotEmployedNorSelfEmployed()
    f(claimData)
    val incomePage = new GYourIncomePage(context) goToThePage()
    incomePage.fillPageWith(claimData)
    incomePage.submitPage()
  }

  def fillSelfEmployed(context: PageObjectsContext, f: => TestData => Unit) = {
    val claimData = s12ClaimDate()
    claimData.EmploymentHaveYouBeenEmployedAtAnyTime_0 = "No"
    claimData.EmploymentHaveYouBeenSelfEmployedAtAnyTime = "Yes"
    claimData.YourIncomeNone = "true"
    claimData.SelfEmployedTypeOfWork = "Some text about rental income"
    claimData.SelfEmployedMoreThanYearAgo = "Yes"
    claimData.SelfEmployedAreYouSelfEmployedNow = "Yes"
    claimData.SelfEmployedHaveAccounts = "Yes"
    f(claimData)
    val incomePage = new GYourIncomePage(context) goToThePage()
    val selfEmploymentPage=incomePage.fillPageWith(claimData).submitPage()
    selfEmploymentPage.fillPageWith(claimData)
    selfEmploymentPage.submitPage()
  }

  def fillRentalIncome(context: PageObjectsContext, f: => TestData => Unit) = {
    val claimData = s12ClaimDate()
    claimData.EmploymentHaveYouBeenSelfEmployedAtAnyTime = "No"
    claimData.EmploymentHaveYouBeenEmployedAtAnyTime_0 = "No"
    claimData.YourIncomeRentalIncome = "true"
    claimData.RentalIncomeInfo = "Some text about rental income"
    f(claimData)

    val incomePage=new GYourIncomePage(context) goToThePage()
    val rentalIncomePage=incomePage.fillPageWith(claimData).submitPage()
    rentalIncomePage.fillPageWith(claimData).submitPage()
  }

  def fillOtherIncome(context: PageObjectsContext, f: => TestData => Unit) = {
    val claimData = s12ClaimDate()
    claimData.EmploymentHaveYouBeenSelfEmployedAtAnyTime = "No"
    claimData.EmploymentHaveYouBeenEmployedAtAnyTime_0 = "No"
    claimData.YourIncomeAnyOtherPay = "true"
    claimData.OtherIncomeInfo = "Some text for other income"
    f(claimData)

    val incomePage=new GYourIncomePage(context) goToThePage()
    val otherIncomePage=incomePage.fillPageWith(claimData).submitPage()
    otherIncomePage.fillPageWith(claimData).submitPage()
  }
}

trait GYourIncomePageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GYourIncomePage (PageObjectsContext(browser))
}

