package utils.pageobjects.s10_information

import utils.WithBrowser
import utils.pageobjects._

/**
 * Page Object for S10_2 G1 Additional Information.
 * @author Jorge Migueis
 *         Date: 02/08/2013
 */
class G1AdditionalInfoPage (ctx:PageObjectsContext) extends ClaimPage(ctx, G1AdditionalInfoPage.url) {
  declareYesNo("#welshCommunication", "ConsentDeclarationCommunicationWelsh")
  declareYesNo("#anythingElse_answer", "ConsentDeclarationTellUsAnythingElseAnswerAboutClaim")
  declareInput("#anythingElse_text", "ConsentDeclarationTellUsAnythingElseTextAboutClaim")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object G1AdditionalInfoPage {
  val url = "/information/additional-info"

  def apply(ctx:PageObjectsContext,previousPage: Option[Page] = None) = new G1AdditionalInfoPage(ctx)
}

/** The context for Specs tests */
trait G1AdditionalInfoPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G1AdditionalInfoPage (PageObjectsContext(browser))
}