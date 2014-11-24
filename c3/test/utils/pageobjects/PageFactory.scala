package utils.pageobjects

import org.fluentlenium.core.Fluent
import play.api.test.TestBrowser


/**
 * Base class of all the PageFactories, used by utils.pageobjects.Page to determine thanks to a page title which Page Object to build.
 * @author Jorge Migueis
 *         Date: 21/08/2013
 */
trait PageFactory {
  def buildPageFromTitle(title: String,ctx:PageObjectsContext):Page

  def buildPageFromFluent(fluent: Fluent): Page = {
    buildPageFromTitle(fluent.title(),PageObjectsContext(TestBrowser(fluent.getDriver,Some(fluent.title))))
  }

}
