package utils.pageobjects.s_self_employment

import utils.WithBrowser
import utils.pageobjects._

final class GAboutSelfEmploymentPage(ctx:PageObjectsContext) extends ClaimPage(ctx, GAboutSelfEmploymentPage.url) {
  declareYesNo("#areYouSelfEmployedNow", "SelfEmployedAreYouSelfEmployedNow")
  declareDate("#whenDidYouStartThisJob", "SelfEmployedWhenDidYouStartThisJob")
  declareDate("#whenDidTheJobFinish", "SelfEmployedWhenDidTheJobFinish")
  declareYesNo("#haveYouCeasedTrading", "SelfEmployedHaveYouCeasedTrading")
  declareInput("#natureOfYourBusiness", "SelfEmployedNatureofYourBusiness")
}

object GAboutSelfEmploymentPage {
  val url = "/self-employment/about-self-employment"

  def apply(ctx:PageObjectsContext) = new GAboutSelfEmploymentPage(ctx)
}

trait GAboutSelfEmploymentPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GAboutSelfEmploymentPage (PageObjectsContext(browser))
}
