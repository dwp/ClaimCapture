package utils.pageobjects.tests

import org.specs2.mutable.Specification
import utils.pageobjects.{PageObjectsContext, CircumstancesPageFactory, UnknownPage, ClaimPageFactory}
import utils.pageobjects.s1_carers_allowance.G1BenefitsPage
import utils.pageobjects.circumstances.s1_about_you.G1ReportAChangeInYourCircumstancesPage
import com.sun.corba.se.impl.oa.poa.POACurrent

/**
 * To change this template use Preferences | File and Code Templates.
 * @author Jorge Migueis
 *         Date: 12/07/2013
 */
class PageFactorySpec extends Specification {

  "The PageFactory" should {
    "Return an UnknownPage if it does not recognise title" in  new MockPageContext {
      val newPage = ClaimPageFactory buildPageFromTitle("Not a known title",PageObjectsContext(browser))
      newPage must beAnInstanceOf[UnknownPage]
    }  
    
    "Return a BenefitPage if provided Benefits page title" in new MockPageContext {
      val newPage = ClaimPageFactory buildPageFromTitle(G1BenefitsPage.title,PageObjectsContext(browser))
      newPage must beAnInstanceOf[G1BenefitsPage]
    }

    "Return an AboutYouPage if provided AboutYouPage page title" in new MockPageContext {
      val newPage = CircumstancesPageFactory buildPageFromTitle(G1ReportAChangeInYourCircumstancesPage.title,PageObjectsContext(browser))
      newPage must beAnInstanceOf[G1ReportAChangeInYourCircumstancesPage]
    }
  }

}
