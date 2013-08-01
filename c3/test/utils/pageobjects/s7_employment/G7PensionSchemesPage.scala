package utils.pageobjects.s7_employment

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

final class G7PensionSchemesPage(browser: TestBrowser, previousPage: Option[Page] = None, iteration: Int) extends Page(browser, G7PensionSchemesPage.url, G7PensionSchemesPage.title, previousPage) {
  override val url = super.getUrl.replace(":jobID",iteration.toString)
  def fillPageWith(theClaim: ClaimScenario) {
    fillYesNo("#payOccupationalPensionScheme", theClaim.selectDynamic("EmploymentDoYouPayTowardsanOccupationalPensionScheme_"+iteration))
    fillInput("#howMuchPension", theClaim.selectDynamic("EmploymentHowMuchYouPayforOccupationalPension_"+iteration))
    fillSelect("#howOftenPension", theClaim.selectDynamic("EmploymentHowOftenOccupationalPension_"+iteration))
    fillYesNo("#payPersonalPensionScheme", theClaim.selectDynamic("EmploymentDoYouPayTowardsAPersonalPension_"+iteration))
    fillInput("#howMuchPersonal", theClaim.selectDynamic("EmploymentHowMuchYouPayforPersonalPension_"+iteration))
    fillSelect("#howOftenPersonal", theClaim.selectDynamic("EmploymentHowOftenPersonalPension_"+iteration))
  }
}

object G7PensionSchemesPage {
  val title = "Pension schemes - Employment"
  val url  = "/employment/pensionSchemes/:jobID"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None, iteration: Int) = new G7PensionSchemesPage(browser,previousPage,iteration)
}

trait G7PensionSchemesPageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = G7PensionSchemesPage buildPageWith(browser,iteration = 1)
}
