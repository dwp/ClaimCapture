package controllers.feedback

import com.fasterxml.jackson.databind.ObjectMapper
import controllers.ClaimScenarioFactory
import gov.dwp.carers.feedback.FeedbackCacheObject
import models.view.cache.EncryptedCacheHandling
import org.joda.time.DateTime
import org.specs2.mutable._
import utils.WithJsBrowser
import utils.pageobjects._
import utils.pageobjects.feedback.GFeedbackPage

class GFeedbackIntegrationSpec extends Specification {
  sequential

  val SatisfiedVSScore = 5

  section("integration", models.domain.Feedback.id)

  "Feedback page" should {
    "be presented" in new WithJsBrowser with PageObjects {
      val page = GFeedbackPage(context)
      page goToThePage()
    }

    "contain errors on invalid submission" in new WithJsBrowser with PageObjects {
      val page = GFeedbackPage(context)
      page goToThePage()
      val nextPage = page submitPage()
      nextPage must beAnInstanceOf[GFeedbackPage]
      nextPage.source must contain("How satisfied were you with the service today? - You must complete this section")
    }

    "navigate to next page on valid submission" in new WithJsBrowser with PageObjects {
      val page = GFeedbackPage(context)
      page goToThePage()
      page fillPageWith ClaimScenarioFactory.feedbackSatisfiedVS
      val nextPage = page submitPage()

      // default post submit url for GB is redirect to gov page
      nextPage.getUrl must contain("/anonymous-feedback/thankyou")
    }

    "add feedback item to memcache list and set json formatted string" in new WithJsBrowser with PageObjects {
      clearFBCache
      val page = GFeedbackPage(context)
      page goToThePage()
      page fillPageWith ClaimScenarioFactory.feedbackSatisfiedVS
      val nextPage = page submitPage()
      val encryptedCacheHandling = new EncryptedCacheHandling() {
        val cacheKey = "1234"
      }
      val fbkeylist = encryptedCacheHandling.getFeedbackList()
      fbkeylist mustNotEqual ("")
      fbkeylist must not contain (",")

      val jsonString = encryptedCacheHandling.getFeedbackFromCache(fbkeylist)
      jsonString must startWith("{")
      jsonString must endWith("}")
    }

    "add feedback item to memcache with correct core data values" in new WithJsBrowser with PageObjects {
      clearFBCache
      val page = GFeedbackPage(context)
      page goToThePage()
      page fillPageWith ClaimScenarioFactory.feedbackSatisfiedVS
      val nextPage = page submitPage()

      val feedbackFromCache = getFeedbackFromCache
      feedbackFromCache.getSatisfiedScore mustEqual (SatisfiedVSScore)
      feedbackFromCache.getOrigin mustEqual ("GB")
      val secsOneMinuteAgo = new DateTime().minusMinutes(1).getMillis / 1000
      val secsNow = new DateTime().getMillis / 1000
      feedbackFromCache.getDatesecs must between(secsOneMinuteAgo, secsNow)
      feedbackFromCache.getUseragent must contain("Mozilla")
    }

    "add feedback item to memcache claimOrCircs set correctly for Claim" in new WithJsBrowser with PageObjects {
      clearFBCache
      browser.goTo("/feedback/feedback")
      browser.pageSource() must contain("id=\"satisfiedAnswer_VS\"")
      browser.click("#satisfiedAnswer_VS")
      browser.pageSource() must contain("id=\"send\"")
      browser.click("#send")

      val feedbackFromCache = getFeedbackFromCache
      feedbackFromCache.getClaimOrCircs mustEqual ("Claim")
    }

    "add feedback item to memcache claimOrCircs set correctly for Circs" in new WithJsBrowser with PageObjects {
      clearFBCache
      browser.goTo("/circumstances/feedback")
      browser.pageSource() must contain("id=\"satisfiedAnswer_VS\"")
      browser.click("#satisfiedAnswer_VS")
      browser.pageSource() must contain("id=\"send\"")
      browser.click("#send")

      val feedbackFromCache = getFeedbackFromCache
      feedbackFromCache.getClaimOrCircs mustEqual ("Circs")
    }
  }
  section("integration", models.domain.ThirdParty.id)

  def clearFBCache() = {
    val encryptedCacheHandling = new EncryptedCacheHandling() {
      val cacheKey = "1234"
    }
    encryptedCacheHandling.removeFeedbackList()
  }
  def getFeedbackFromCache() = {
    val encryptedCacheHandling = new EncryptedCacheHandling() {
      val cacheKey = "1234"
    }
    val fbkeylist = encryptedCacheHandling.getFeedbackList()
    val jsonString = encryptedCacheHandling.getFeedbackFromCache(fbkeylist)
    val objectMapper: ObjectMapper = new ObjectMapper
    val cacheObject = objectMapper.readValue(jsonString, classOf[FeedbackCacheObject])
    cacheObject
  }
}
