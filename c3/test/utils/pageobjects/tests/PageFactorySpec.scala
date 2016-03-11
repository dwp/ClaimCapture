package utils.pageobjects.tests

import org.specs2.mutable._
import utils.WithApplication
import utils.pageobjects.{PageObjectsContext, CircumstancesPageFactory, UnknownPage, ClaimPageFactory}
import utils.pageobjects.s_eligibility.GBenefitsPage
import utils.pageobjects.circumstances.start_of_process.GCircsYourDetailsPage

/**
 * To change this template use Preferences | File and Code Templates.
 * @author Jorge Migueis
 *         Date: 12/07/2013
 */
class PageFactorySpec extends Specification {
  section("unit")
  "The PageFactory" should {
    "Return an UnknownPage if it does not recognise title" in new WithApplication with MockPageContext {
      val newPage = ClaimPageFactory buildPageFromUrl("/unknown",PageObjectsContext(browser))
      newPage must beAnInstanceOf[UnknownPage]
    }  
    
    "Return a BenefitPage if provided Benefits page title" in new WithApplication with MockPageContext {
      val newPage = ClaimPageFactory buildPageFromUrl(GBenefitsPage.url,PageObjectsContext(browser))
      newPage must beAnInstanceOf[GBenefitsPage]
    }

    "Return an AboutYouPage if provided AboutYouPage page title" in new WithApplication with MockPageContext {
      val newPage = CircumstancesPageFactory buildPageFromUrl(GCircsYourDetailsPage.url,PageObjectsContext(browser))
      newPage must beAnInstanceOf[GCircsYourDetailsPage]
    }
  }
  section("unit")
}
