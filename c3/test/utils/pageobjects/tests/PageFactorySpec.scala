package utils.pageobjects.tests

import org.specs2.mutable.Specification
import utils.pageobjects.PageFactory

/**
 * To change this template use Preferences | File and Code Templates.
 * @author Jorge Migueis
 *         Date: 12/07/2013
 */
class PageFactorySpec  extends Specification {

  "The PageFactory" should {

    "be able to build a claim from a valid csv file" in {
      PageFactory buildClaimFromFile("/tests.csv")
    }
  }

}
