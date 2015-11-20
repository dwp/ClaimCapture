package utils.pageobjects.s_information

import utils.WithBrowser
import utils.pageobjects._

/**
 * Page Object for S10_2 G1 Additional Information.
 * @author Jorge Migueis
 *         Date: 02/08/2013
 */
class GAdditionalInfoPage (ctx:PageObjectsContext) extends ClaimPage(ctx, GAdditionalInfoPage.url) {
  declareYesNo("#welshCommunication", "ConsentDeclarationCommunicationWelsh")
  declareYesNo("#anythingElse_answer", "ConsentDeclarationTellUsAnythingElseAnswerAboutClaim")
  declareInput("#anythingElse_text", "ConsentDeclarationTellUsAnythingElseTextAboutClaim")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object GAdditionalInfoPage {
  val url = "/information/additional-info"

  def apply(ctx:PageObjectsContext,previousPage: Option[Page] = None) = new GAdditionalInfoPage(ctx)
}

/** The context for Specs tests */
trait GAdditionalInfoPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GAdditionalInfoPage (PageObjectsContext(browser))
}
