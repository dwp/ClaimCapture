package utils.pageobjects.s1_carers_allowance

import play.api.test.WithBrowser
import utils.pageobjects._

final class G4LivesInGBPage(ctx:PageObjectsContext) extends ClaimPage(ctx, G4LivesInGBPage.url, G4LivesInGBPage.title) {
  declareYesNo("#livesInGB_answer", "CanYouGetCarersAllowanceDoYouNormallyLiveinGb")
}

object G4LivesInGBPage {
  val title = "Do you live in England, Scotland or Wales? - Can you get Carer's Allowance?".toLowerCase

  val url = "/allowance/lives-in-gb"

  def apply(ctx:PageObjectsContext) = new G4LivesInGBPage(ctx)
}

trait G4LivesInGBPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G4LivesInGBPage (PageObjectsContext(browser))
}