package utils.pageobjects.tests

import org.specs2.mutable.Specification
import utils.pageobjects.{UnknownPage, PageFactory}
import utils.pageobjects.s1_carers_allowance.G1BenefitsPage

/**
 * To change this template use Preferences | File and Code Templates.
 * @author Jorge Migueis
 *         Date: 12/07/2013
 */
class PageFactorySpec extends Specification {

  "The PageFactory" should {
    "Return an UnknownPage if it does not recognise title" in  new MockPageContext {
      val newPage = PageFactory buildPageFromTitle(browser, "Not a known title", None,1)
      newPage must beAnInstanceOf[UnknownPage]
    }  
    
    "Return a BenefitPage if provided Benefits page title" in new MockPageContext {
      val newPage = PageFactory buildPageFromTitle(browser, G1BenefitsPage.title,None,1)
      newPage must beAnInstanceOf[G1BenefitsPage]
    }
  }

}
