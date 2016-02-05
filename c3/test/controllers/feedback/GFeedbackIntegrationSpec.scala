package controllers.feedback

import com.fasterxml.jackson.databind.ObjectMapper
import controllers.ClaimScenarioFactory
import models.view.cache.EncryptedCacheHandling
import org.joda.time.DateTime
import org.specs2.mutable._
import utils.WithJsBrowser
import utils.pageobjects._
import utils.pageobjects.feedback.GFeedbackPage

class GFeedbackIntegrationSpec extends Specification {
  sequential

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

    /* ColinG Jan2016. Warning ... ths test will run under EhCache by default which will autoclear for each test instance. If run under memcache values may persist
    across test runs meaning we cannot identify the newly added key to fblist. Its easy currently, as its the only item in the list
    In which case keylist will be FB-XXXX,FB-YYY ... and  fbkeylist must notContain(",") will fail.
    */
    "add feedback item to memcache list and set json formatted string" in new WithJsBrowser with PageObjects {
      val page = GFeedbackPage(context)
      page goToThePage()
      page fillPageWith ClaimScenarioFactory.feedbackSatisfiedVS

      val encryptedCacheHandling = new EncryptedCacheHandling() {
        val cacheKey = "1234"
      }
      val fbkeylist = encryptedCacheHandling.getFeedbackList()
      fbkeylist mustNotEqual ("")
      fbkeylist must not contain(",")

      val jsonString = encryptedCacheHandling.getFeedbackFromCache(fbkeylist)
      jsonString must startWith("{")
      jsonString must endWith("}")
    }

    "add feedback item to memcache with correct core data values" in new WithJsBrowser with PageObjects {
      val page = GFeedbackPage(context)
      page goToThePage()
      page fillPageWith ClaimScenarioFactory.feedbackSatisfiedVS

      val encryptedCacheHandling = new EncryptedCacheHandling() {
        val cacheKey = "1234"
      }
      val fbkeylist = encryptedCacheHandling.getFeedbackList()
      fbkeylist mustNotEqual ("")
      fbkeylist must not contain(",")

      val jsonString = encryptedCacheHandling.getFeedbackFromCache(fbkeylist)
      val objectMapper: ObjectMapper = new ObjectMapper
      val cacheObject = objectMapper.readValue(jsonString, classOf[FeedbackCacheObject])
      val SatisfiedVSScore = 5
      cacheObject.getSatisfiedScore mustEqual (5)
      cacheObject.getOrigin mustEqual ("GB")
      val secsOneMinuteAgo = new DateTime().minusMinutes(1).getMillis / 1000
      val secsNow = new DateTime().getMillis / 1000
      cacheObject.getDatesecs must between(secsOneMinuteAgo, secsNow)
      cacheObject.getUseragent must contain("Mozilla")
    }
  }
  section("integration", models.domain.ThirdParty.id)
}
