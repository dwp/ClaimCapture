package utils.pageobjects.s11_pay_details

import play.api.test.WithBrowser
import utils.pageobjects._

/**
 * To change this template use Preferences | File and Code Templates.
 * @author Jorge Migueis
 *         Date: 18/07/2013
 */
class G1HowWePayYouPage(ctx:PageObjectsContext) extends ClaimPage(ctx, G1HowWePayYouPage.url) {
  declareRadioList("#likeToPay", "HowWePayYouHowWouldYouLikeToGetPaid")
  declareRadioList("#paymentFrequency", "HowWePayYouHowOftenDoYouWantToGetPaid")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object d
 * efined in Page.scala
 */
object G1HowWePayYouPage {
  val url  = "/pay-details/how-we-pay-you"

  def apply(ctx:PageObjectsContext) = new G1HowWePayYouPage(ctx)
}

/** The context for Specs tests */
trait G1HowWePayYouPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G1HowWePayYouPage (PageObjectsContext(browser))
}
