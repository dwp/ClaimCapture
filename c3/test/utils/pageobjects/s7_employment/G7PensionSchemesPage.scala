package utils.pageobjects.s7_employment

import play.api.test.WithBrowser
import utils.pageobjects._

final class G7PensionSchemesPage(ctx:PageObjectsContext, iteration: Int) extends ClaimPage(ctx, G7PensionSchemesPage.url.replace(":jobID", iteration.toString), G7PensionSchemesPage.title, iteration) {
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

  def apply(ctx:PageObjectsContext, iteration: Int= 1) = new G7PensionSchemesPage(ctx,iteration)
}

trait G7PensionSchemesPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G7PensionSchemesPage (PageObjectsContext(browser),iteration = 1)
}