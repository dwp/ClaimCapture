package utils.pageobjects.s8_self_employment

import play.api.test.WithBrowser
import utils.pageobjects._

final class G1AboutSelfEmploymentPage(ctx:PageObjectsContext) extends ClaimPage(ctx, G1AboutSelfEmploymentPage.url) {
  declareYesNo("#areYouSelfEmployedNow", "SelfEmployedAreYouSelfEmployedNow")
  declareDate("#whenDidYouStartThisJob", "SelfEmployedWhenDidYouStartThisJob")
  declareDate("#whenDidTheJobFinish", "SelfEmployedWhenDidTheJobFinish")
  declareYesNo("#haveYouCeasedTrading", "SelfEmployedHaveYouCeasedTrading")
  declareInput("#natureOfYourBusiness", "SelfEmployedNatureofYourBusiness")
}

object G1AboutSelfEmploymentPage {
  val url = "/self-employment/about-self-employment"

  def apply(ctx:PageObjectsContext) = new G1AboutSelfEmploymentPage(ctx)
}

trait G1AboutSelfEmploymentPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G1AboutSelfEmploymentPage (PageObjectsContext(browser))
}