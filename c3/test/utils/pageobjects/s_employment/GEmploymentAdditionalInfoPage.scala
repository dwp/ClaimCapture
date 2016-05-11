package utils.pageobjects.s_employment

import utils.pageobjects.{PageContext, ClaimPage, PageObjectsContext}
import utils.WithBrowser


final class GEmploymentAdditionalInfoPage (ctx:PageObjectsContext) extends ClaimPage(ctx, GEmploymentAdditionalInfoPage.url) {
  declareYesNo("#empAdditionalInfo_answer", "EmploymentDoYouWantToAddAnythingAboutYourWork")
  declareInput("#empAdditionalInfo_text", "EmploymentAdditionalInfo")
}

object GEmploymentAdditionalInfoPage {
  val url  = "/your-income/employment/additional-info"

  def apply(ctx:PageObjectsContext) = new GEmploymentAdditionalInfoPage(ctx)
}

trait GEmploymentAdditionalInfoPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GEmploymentAdditionalInfoPage (PageObjectsContext(browser))
}
