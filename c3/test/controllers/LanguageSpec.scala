package controllers

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithApplication
import play.api.test.FakeRequest
import models.view.{CachedClaim, CachedChangeOfCircs}
import models.domain.{Claim, MockForm}
import play.api.test.Helpers._
import play.api.i18n.Lang
import play.api.cache.Cache

class LanguageSpec extends Specification with Tags {
  "Language - Change Language" should {
    "change of claim present - change language to English" in new WithApplication with MockForm {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
      Cache.set(claimKey, Claim(claimKey, List.empty))

      val expectedLangAsString = "en"
      val expectedLang = Lang(expectedLangAsString)
      val result = controllers.Language.change(expectedLangAsString)(request)
      status(result) mustEqual SEE_OTHER

      val claim = Cache.getAs[Claim](claimKey).get
      claim.lang.get mustEqual expectedLang
    }

    "change of circs present - change language to English" in new WithApplication with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey).withHeaders(REFERER -> "http://localhost:9000/circumstances/identification/about-you")
      Cache.set(claimKey, Claim(claimKey, List.empty))

      val expectedLangAsString = "en"
      val expectedLang = Lang(expectedLangAsString)
      val result = controllers.Language.change(expectedLangAsString)(request)
      status(result) mustEqual SEE_OTHER

      val claim = Cache.getAs[Claim](claimKey).get
      claim.lang.get mustEqual expectedLang
    }

    "change of claim present - change language to Welsh" in new WithApplication with MockForm {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
      Cache.set(claimKey, Claim(claimKey, List.empty))

      val expectedLangAsString = "cy"
      val expectedLang = Lang(expectedLangAsString)
      val result = controllers.Language.change(expectedLangAsString)(request)
      status(result) mustEqual SEE_OTHER

      val claim = Cache.getAs[Claim](claimKey).get
      claim.lang.get mustEqual expectedLang
    }

    "change of circs present - change language to Welsh" in new WithApplication with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey).withHeaders(REFERER -> "http://localhost:9000/circumstances/identification/about-you")
      Cache.set(claimKey, Claim(claimKey, List.empty))

      val expectedLangAsString = "cy"
      val expectedLang = Lang(expectedLangAsString)
      val result = controllers.Language.change(expectedLangAsString)(request)
      status(result) mustEqual SEE_OTHER

      val claim = Cache.getAs[Claim](claimKey).get
      claim.lang.get mustEqual expectedLang
    }

  } section("unit")
}
