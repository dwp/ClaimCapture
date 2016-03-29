package utils.pageobjects.s_self_employment

import utils.WithBrowser
import utils.pageobjects._

final class GSelfEmploymentDatesPage(ctx:PageObjectsContext) extends ClaimPage(ctx, GSelfEmploymentDatesPage.url) {
/*  declareYesNo("#areYouSelfEmployedNow", "SelfEmployedAreYouSelfEmployedNow")
  declareDate("#whenDidYouStartThisJob", "SelfEmployedWhenDidYouStartThisJob")
  declareDate("#whenDidTheJobFinish", "SelfEmployedWhenDidTheJobFinish")
  declareYesNo("#haveYouCeasedTrading", "SelfEmployedHaveYouCeasedTrading")
  declareInput("#natureOfYourBusiness", "SelfEmployedNatureofYourBusiness")
  */
}

object GSelfEmploymentDatesPage {
  val url = "/self-employment/self-employment-dates"

  def apply(ctx:PageObjectsContext) = new GSelfEmploymentDatesPage(ctx)
}

trait GSelfEmploymentDatesPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GSelfEmploymentDatesPage (PageObjectsContext(browser))
}
