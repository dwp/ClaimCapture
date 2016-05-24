package utils.pageobjects.tests

import org.specs2.mutable._
import play.api.Play
import utils.WithApplication
import utils.pageobjects._

/**
 * Basic unit test of Page.
 * @author Jorge Migueis
 *         Date: 10/07/2013
 */
class PageSpec extends Specification {
  section("unit")
  "Page" should {
    "be able to go the underlying html page" in new WithApplication with MockPageContext {
      println("PlayUnsafe:"+Play.unsafeApplication)
    }
/*
    "allow submit and returns a page" in new WithApplication with MockPageContext {
      page.goToThePage()
      val nextPage = page submitPage()
      there was one(browser).submit("button[type='submit']")
      there was one(browser).find("div[class=validation-summary] ol li")
      nextPage must haveClass[UnknownPage]
    }

    "cannot submit same page twice if no errors. The framework throws an exception." in new WithApplication with MockPageContext {
      page.goToThePage()
      page submitPage()
      page submitPage() must throwA[PageObjectException]
      there was one(browser).submit("button[type='submit']")
    }

    "allow running claim" in new WithApplication with MockPageContext {
      page goToThePage()
      page fillPageWith new TestData
      val nextPage = page submitPage()
      there was one(browser).submit("button[type='submit']")
      there was one(browser).find("div[class=validation-summary] ol li")
    }

    "Throw exception if cannot go to the right page" in new WithApplication with MockPageContext {
      page.goBack()
      page submitPage() must throwA[PageObjectException]
    }

    "be able to provide full path of pages used to reach the current page." in new WithApplication with MockPageContext {
      val page2 = new MockPage(PageObjectsContext(browser,IterationManager(),Some(page)))
      val page3 = new MockPage(PageObjectsContext(browser,IterationManager(),Some(page2)))
      page3.fullPagePath mustEqual s"${MockPage.url} < ${MockPage.url} < ${MockPage.url}"
    }
    */
  }
  section("unit")
}
