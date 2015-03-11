package utils.filters

import models.view.CachedClaim
import org.specs2.mutable.Specification
import play.api.cache.Cache
import play.api.libs.iteratee.Iteratee
import play.api.mvc.{Result, RequestHeader, EssentialFilter, EssentialAction}
import play.api.test.{FakeRequest, WithApplication}
import play.mvc.Http.HeaderNames


class UserAgentCheckActionSpec extends Specification {

  "User Agent Action/Filter" should {
    "not check start page, but should extract User Agent and store it if POST" in new WithApplication {
      val keyValue = "aoisdyfiuasdyf"
      val agent = "A test agent"
      val request = FakeRequest(method = "POST", path = "/allowance/benefits")
        .withSession(CachedClaim.key -> keyValue)
        .withHeaders(HeaderNames.ACCEPT -> "text/html", HeaderNames.USER_AGENT -> agent)
      UserAgentCheckAction.defaultSetIf(request) must beTrue
      val filter = UserAgentCheckFilter()
      val mockNextAction = new MockAction
      val action = filter(mockNextAction)
      action(request)
      mockNextAction.called must beTrue
      Cache.getAs[String](keyValue + "_UA").get mustEqual agent
    }

    "check a page if not start apeg or end page for GET" in new WithApplication {
      val keyValue = "aoisdyfiuasdyf"
      val agent = "A test agent"
      Cache.set(keyValue + "_UA", agent)
      val request = FakeRequest(method = "GET", path = "/about-you/abroad-for-more-than-52-weeks")
        .withSession(CachedClaim.key -> keyValue)
        .withHeaders(HeaderNames.ACCEPT -> "text/html", HeaderNames.USER_AGENT -> agent)
      UserAgentCheckAction.defaultCheckIf(request) must beTrue
      val filter = UserAgentCheckFilter()
      val mockNextAction = new MockAction
      val action = filter(mockNextAction)
      action(request)
      mockNextAction.called must beTrue
    }


    "check a page if not start apeg or end page for POST" in new WithApplication {
      val keyValue = "aoisdyfiuasdyf"
      val agent = "A test agent"
      Cache.set(keyValue + "_UA", agent)
      val request = FakeRequest(method = "POST", path = "/employment/employment")
        .withSession(CachedClaim.key -> keyValue)
        .withHeaders(HeaderNames.ACCEPT -> "text/html", HeaderNames.USER_AGENT -> agent)
      UserAgentCheckAction.defaultCheckIf(request) must beTrue
      val filter = UserAgentCheckFilter()
      val mockNextAction = new MockAction
      val action = filter(mockNextAction)
      action(request)
      mockNextAction.called must beTrue
    }

    "remove User Agent from cache when reaches an end page" in new WithApplication {
      val keyValue = "aoisdyfiuasdyf"
      val agent = "A test agent"
      Cache.set(keyValue + "_UA", agent)
      val request = FakeRequest(method = "GET", path = "/timeout")
        .withSession(CachedClaim.key -> keyValue)
        .withHeaders(HeaderNames.ACCEPT -> "text/html", HeaderNames.USER_AGENT -> agent)
      UserAgentCheckAction.defautRemoveIf(request) must beTrue
      val filter = UserAgentCheckFilter()
      val mockNextAction = new MockAction
      val action = filter(mockNextAction)
      action(request)
      mockNextAction.called must beTrue
      Cache.getAs[String](keyValue + "_UA") must beNone
    }

  }

}

class MockAction extends EssentialAction {

  var called = false

  override def apply(v1: RequestHeader): Iteratee[Array[Byte], Result] = {
    called = true
    null
  }
}
