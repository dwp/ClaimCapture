package utils.pageobjects.tests

import org.specs2.mutable.Specification
import utils.pageobjects.{PageObjectsContext, CircumstancesPageFactory, UnknownPage, ClaimPageFactory}
import utils.pageobjects.s_eligibility.GBenefitsPage
import utils.pageobjects.circumstances.s1_start_of_process.{G2ReportAChangeInYourCircumstancesPage, G2ReportAChangeInYourCircumstancesPage$}
import com.sun.corba.se.impl.oa.poa.POACurrent

/**
 * To change this template use Preferences | File and Code Templates.
 * @author Jorge Migueis
 *         Date: 12/07/2013
 */
class PageFactorySpec extends Specification {

  "The PageFactory" should {
    "Return an UnknownPage if it does not recognise title" in  new MockPageContext {
      val newPage = ClaimPageFactory buildPageFromUrl("/unknown",PageObjectsContext(browser))
      newPage must beAnInstanceOf[UnknownPage]
    }  
    
    "Return a BenefitPage if provided Benefits page title" in new MockPageContext {
      val newPage = ClaimPageFactory buildPageFromUrl(GBenefitsPage.url,PageObjectsContext(browser))
      newPage must beAnInstanceOf[GBenefitsPage]
    }

    "Return an AboutYouPage if provided AboutYouPage page title" in new MockPageContext {
      val newPage = CircumstancesPageFactory buildPageFromUrl(G2ReportAChangeInYourCircumstancesPage.url,PageObjectsContext(browser))
      newPage must beAnInstanceOf[G2ReportAChangeInYourCircumstancesPage]
    }
  }

}
