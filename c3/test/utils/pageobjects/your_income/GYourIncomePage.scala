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
  declareCheck("#yourIncome_anyother", "YourIncomeAnyOtherPay")
  declareCheck("#yourIncome_none", "YourIncomeNone")
}

object GYourIncomePage {
  val url  = "/your-income/your-income"

  def apply(ctx:PageObjectsContext) = new GYourIncomePage(ctx)

  def fillYourIncomes(context: PageObjectsContext, f: => TestData => Unit) = {
    val claimData = s7NotEmployedNorSelfEmployed()
    claimData.YourIncomeStatutorySickPay = ""
    f(claimData)
    val employment = new GYourIncomePage(context) goToThePage()
    employment.fillPageWith(claimData)
    employment.submitPage()
  }
}

trait GYourIncomePageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GYourIncomePage (PageObjectsContext(browser))
}

