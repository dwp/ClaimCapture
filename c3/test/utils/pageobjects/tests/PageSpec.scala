package utils.pageobjects.tests

import org.specs2.mutable._
import utils.pageobjects._

/**
 * Basic unit test of Page.
 * @author Jorge Migueis
 *         Date: 10/07/2013
 */
class PageSpec extends Specification {

  "Page" should {

    "be able to go the underlying html page" in new MockPageContext {
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

    "cannot submit same page twice if no errors. The framework throws an exception." in new MockPageContext {
      page.goToThePage()
      page submitPage()
      page submitPage() must throwA[PageObjectException]
      there was one(browser).submit("button[type='submit']")
    }


    "allow running claim" in new MockPageContext {
      page goToThePage()
      page runClaimWith (new TestData, MockPage.url)
      there was one(browser).submit("button[type='submit']")
      there was one(browser).find("div[class=validation-summary] ol li")
    }

    "Throw exception if cannot go to the right page" in new MockPageWrongTitleContext {
      page.goToThePage() must throwA[PageObjectException]
    }

    "be able to provide full path of pages used to reach the current page." in new MockPageContext {
      val page2 = new MockPage(PageObjectsContext(browser,IterationManager(),Some(page)))
      val page3 = new MockPage(PageObjectsContext(browser,IterationManager(),Some(page2)))
      page3.fullPagePath mustEqual s"${MockPage.url} < ${MockPage.url} < ${MockPage.url}"
    }

  }
}
