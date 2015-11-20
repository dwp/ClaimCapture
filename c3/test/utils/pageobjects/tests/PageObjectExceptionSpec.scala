package utils.pageobjects.tests

import org.specs2.mutable._
import utils.pageobjects.PageObjectException

/**
 * Unit test of PageObjectException                                  .
 * @author Jorge Migueis
 *         Date: 11/07/2013
 */
class PageObjectExceptionSpec extends Specification {

  "PageObjectException" should {

    "create the exception message by aggregating all the error messages passed as argument" in {
      val otherMessages = List("Other1", "Other2", "Other3")
      val exception = new PageObjectException("First Message", otherMessages)
      exception getMessage() mustEqual "First Message. Other1. Other2. Other3"
    }
  }
}
