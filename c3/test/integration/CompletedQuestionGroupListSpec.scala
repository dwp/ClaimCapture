package integration

import play.api.test.WithBrowser
import org.specs2.mutable.Specification
import org.specs2.mutable.Tags

class CompletedQuestionGroupListSpec extends Specification with Tags {

  "Completed Question Group List" should {
    "increase when navigating forwards" in new WithBrowser {
      Helper.fillTheirPersonalDetails(browser)
      Helper.fillTheirContactDetails(browser)
      browser.find("div[class=completed] ul li").size mustEqual 2
      
      Helper.fillMoreAboutThePersonWithClaimedAllowanceBefore(browser)
      
      browser.find("div[class=completed] ul li").size mustEqual 3
    }
    
    "decrease when navigating backwards" in new WithBrowser {
      Helper.fillTheirPersonalDetails(browser)
      Helper.fillTheirContactDetails(browser)
      Helper.fillMoreAboutThePersonWithClaimedAllowanceBefore(browser)
      browser.find("div[class=completed] ul li").size mustEqual 3
      
      browser.click("#backButton")
      
      browser.find("div[class=completed] ul li").size mustEqual 2
    }

    "contain the correct items when navigating S4G3 ClaimedAllowanceBefore positive answer path" in new WithBrowser {
      Helper.fillTheirPersonalDetails(browser)
      Helper.fillTheirContactDetails(browser)
      Helper.fillMoreAboutThePersonWithClaimedAllowanceBefore(browser)
      Helper.fillPreviousCarerPersonalDetails(browser)
      Helper.fillPreviousCarerContactDetails(browser)
      Helper.fillRepresentativesForThePerson(browser)
      
      browser.find("div[class=completed] ul li").size mustEqual 6
      browser.find("div[class=completed] ul li").get(2).getText must contain("More about the person you care for")
      browser.find("div[class=completed] ul li").get(3).getText must contain("About the previous Carer")
      browser.find("div[class=completed] ul li").get(4).getText must contain("More about the care you provide")
      browser.find("div[class=completed] ul li").get(5).getText must contain("Representatives for the person you care for")
    }
    
    "contain the correct items when navigating S4G3 ClaimedAllowanceBefore negative answer path" in new WithBrowser {
      Helper.fillTheirPersonalDetails(browser)
      Helper.fillTheirContactDetails(browser)
      Helper.fillMoreAboutThePersonWithNotClaimedAllowanceBefore(browser)
      Helper.fillRepresentativesForThePerson(browser)
      
      browser.find("div[class=completed] ul li").size mustEqual 4
      browser.find("div[class=completed] ul li").get(2).getText must contain("More about the person you care for")
      browser.find("div[class=completed] ul li").get(3).getText must contain("Representatives for the person you care for")
    }
    
    "remove invalidated history when the user completes the S4G3 ClaimedAllowanceBefore postive answer path but goes back and changes to the negative answer path" in new WithBrowser {
      Helper.fillTheirPersonalDetails(browser)
      Helper.fillTheirContactDetails(browser)
      Helper.fillMoreAboutThePersonWithClaimedAllowanceBefore(browser)
      Helper.fillPreviousCarerPersonalDetails(browser)
      Helper.fillPreviousCarerContactDetails(browser)
      Helper.fillRepresentativesForThePerson(browser)

      browser.click("#backButton")
      browser.click("#backButton")
      Helper.fillMoreAboutThePersonWithNotClaimedAllowanceBefore(browser)
      Helper.fillRepresentativesForThePerson(browser)
      
      browser.find("div[class=completed] ul li").size mustEqual 4
      browser.find("div[class=completed] ul li").get(2).getText must contain("More about the person you care for")
      browser.find("div[class=completed] ul li").get(3).getText must contain("Representatives for the person you care for")
    }
  } section "integration"
}