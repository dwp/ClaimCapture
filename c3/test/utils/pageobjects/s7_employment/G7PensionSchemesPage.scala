package utils.pageobjects.s7_employment

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{TestData, ClaimPage, Page, PageContext}

final class G7PensionSchemesPage(browser: TestBrowser, previousPage: Option[Page] = None, iteration: Int) extends ClaimPage(browser, G7PensionSchemesPage.url.replace(":jobID", iteration.toString), G7PensionSchemesPage.title, previousPage, iteration) {
  declareYesNo("#payOccupationalPensionScheme", "EmploymentDoYouPayTowardsanOccupationalPensionScheme_" + iteration)
  declareInput("#howMuchPension", "EmploymentHowMuchYouPayforOccupationalPension_" + iteration)
  declareSelect("#howOftenPension_frequency","EmploymentHowOftenOccupationalPension_" + iteration)
  declareInput("#howOftenPension_frequency_other","EmploymentHowOftenOtherOccupationalPension_" + iteration)
  declareYesNo("#payPersonalPensionScheme", "EmploymentDoYouPayTowardsAPersonalPension_" + iteration)
  declareInput("#howMuchPersonal", "EmploymentHowMuchYouPayforPersonalPension_" + iteration)
  declareSelect("#howOftenPersonal_frequency", "EmploymentHowOftenPersonalPension_" + iteration)
  declareInput("#howOftenPersonal_frequency_other","EmploymentHowOftenOtherPersonalPension_" + iteration)
}

object G7PensionSchemesPage {
  val title = "Pension schemes - Employment History".toLowerCase

  val url  = "/employment/pension-schemes/:jobID"

  def apply(browser: TestBrowser, previousPage: Option[Page] = None, iteration: Int) = new G7PensionSchemesPage(browser,previousPage,iteration)
}

trait G7PensionSchemesPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G7PensionSchemesPage (browser,iteration = 1)
}