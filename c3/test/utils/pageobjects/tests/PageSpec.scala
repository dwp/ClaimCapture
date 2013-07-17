package utils.pageobjects.tests

import org.specs2.mutable.Specification
import utils.pageobjects.{UnknownPage, PageObjectException, ClaimScenario}


/**
 * Basic unit test of Page.
 * @author Jorge Migueis
 *         Date: 10/07/2013
 */
class PageSpec extends Specification {

  "Page" should {

    "be presented" in new MockPageContext {
      page.goToThePage()
      there was one(browser).goTo(anyString)
    }

    "allow submit and returns a page" in new MockPageContext {
      page.goToThePage()
      val nextPage = page submitPage()
      there was one(browser).submit("button[type='submit']")
      there was one(browser).find("div[class=validation-summary] ol li")
      nextPage must haveClass[UnknownPage]
    }


    "allow running claim" in new MockPageContext {
      page goToThePage()
      page runClaimWith (new ClaimScenario, MockPage.title)
      there was one(browser).submit("button[type='submit']")
      there was one(browser).find("div[class=validation-summary] ol li")
    }

    "Throw exception if cannot go to the write page" in new MockPageWrongTitleContext {
      page.goToThePage() must throwA[PageObjectException]
    }
  }
}
