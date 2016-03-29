package utils.pageobjects.s_self_employment

import utils.WithBrowser
import utils.pageobjects._

final class GSelfEmploymentDatesPage(ctx:PageObjectsContext) extends ClaimPage(ctx, GAboutSelfEmploymentPage.url) {
/*  declareYesNo("#areYouSelfEmployedNow", "SelfEmployedAreYouSelfEmployedNow")
  declareDate("#whenDidYouStartThisJob", "SelfEmployedWhenDidYouStartThisJob")
  declareDate("#whenDidTheJobFinish", "SelfEmployedWhenDidTheJobFinish")
  declareYesNo("#haveYouCeasedTrading", "SelfEmployedHaveYouCeasedTrading")
  declareInput("#natureOfYourBusiness", "SelfEmployedNatureofYourBusiness")
  */
}

object GSelfEmploymentDatesPage {
  val url = "/self-employment/self-employment-dates"

  def apply(ctx:PageObjectsContext) = new GAboutSelfEmploymentPage(ctx)
}

trait GSelfEmploymentDatesPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GAboutSelfEmploymentPage (PageObjectsContext(browser))
}
