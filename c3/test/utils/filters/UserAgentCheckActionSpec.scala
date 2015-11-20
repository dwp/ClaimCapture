package utils.filters

import models.view.CachedClaim
import org.specs2.mutable._
import play.api.Play._
import play.api.cache.{CacheApi, Cache}
import play.api.libs.iteratee.Iteratee
import play.api.mvc.{Result, RequestHeader, EssentialAction}
import play.api.test.{FakeApplication, FakeRequest}
import play.mvc.Http.HeaderNames
import utils.WithApplication


class UserAgentCheckActionSpec extends Specification {
  import UserAgentCheckActionSpec._

  "User Agent Action/Filter" should {
    "not check start page, but should extract User Agent and store it if POST" in new WithApplication {
      val cache = current.injector.instanceOf[CacheApi]
      val keyValue = "startuasdyf"
      exerciseFilterFor(keyValue,"POST",  "/allowance/benefits", UserAgentCheckAction.defaultSetIf,expectSuccess=true, presetCache=false)
      cache.get[String](keyValue + "_UA").get mustEqual agent
      exerciseFilterFor(keyValue,"POST",  "/circumstances/report-changes/selection", UserAgentCheckAction.defaultSetIf,expectSuccess=true, presetCache=false)
      cache.get[String](keyValue + "_UA").get mustEqual agent
      exerciseFilterFor(keyValue,"POST",  "/change-language/cy", UserAgentCheckAction.defaultSetIf,expectSuccess=true, presetCache=false)
      cache.get[String](keyValue + "_UA").get mustEqual agent
    }

    "check a page if not start page or end page for GET" in new WithApplication {
      val keyValue = "12kasdfuasdyf"
      exerciseFilterFor(keyValue,"GET",  "/about-you/abroad-for-more-than-52-weeks", UserAgentCheckAction.defaultCheckIf,expectSuccess=true)
    }


    "check a page if not start page or end page for POST" in new WithApplication {
      val keyValue = "sdyfiuaslksdfjdyf"
      exerciseFilterFor(keyValue,"POST",  "/employment/employment", UserAgentCheckAction.defaultCheckIf,expectSuccess=true)
    }

    "not check error page or a report page or assets" in new WithApplication {
      val keyValue = "moisdyighfgsdyf"
      exerciseFilterFor(keyValue,"GET",  "/report/health", UserAgentCheckAction.defaultCheckIf,expectSuccess=false)
      exerciseFilterFor(keyValue,"GET",  "/error", UserAgentCheckAction.defaultCheckIf,expectSuccess=false)
      exerciseFilterFor(keyValue,"GET",  "/assets/images/crown.png", UserAgentCheckAction.defaultCheckIf,expectSuccess=false)
    }

    "not check channel shift pages" in new WithApplication {
      val keyValue = "moisdyighfgsdyf"
      exerciseFilterFor(keyValue,"GET",  "/CS2015", UserAgentCheckAction.defaultCheckIf,expectSuccess=false)
      exerciseFilterFor(keyValue,"GET",  "/cs2015", UserAgentCheckAction.defaultCheckIf,expectSuccess=false)
    }

    "remove User Agent from cache when reaches an end page" in new WithApplication {
      val cache = current.injector.instanceOf[CacheApi]
      val keyValue = "aoisdyfiuasdyf"
      exerciseFilterFor(keyValue,"GET",  "/timeout", UserAgentCheckAction.defautRemoveIf,expectSuccess=true)
      cache.get[String](keyValue + "_UA") must beNone
      exerciseFilterFor(keyValue,"GET",  "/thankyou/apply-carers", UserAgentCheckAction.defautRemoveIf,expectSuccess=true)
      cache.get[String](keyValue + "_UA") must beNone
      exerciseFilterFor(keyValue,"GET",  "/thankyou/change-carers", UserAgentCheckAction.defautRemoveIf,expectSuccess=true)
      cache.get[String](keyValue + "_UA") must beNone
    }

  }

}

object UserAgentCheckActionSpec extends Specification{
  private val agent = "A test agent"

  def exerciseFilterFor(keyValue:String, method:String, path:String, operation:(RequestHeader) => Boolean,
                        expectSuccess:Boolean, presetCache: Boolean = true)(implicit app: FakeApplication) = {
    val cache = current.injector.instanceOf[CacheApi]
    cache.remove(keyValue + "_UA")
    if (presetCache) cache.set(keyValue + "_UA", agent)
    val request = FakeRequest(method = method, path = path)
      .withSession(CachedClaim.key -> keyValue)
      .withHeaders(HeaderNames.ACCEPT -> "text/html", HeaderNames.USER_AGENT -> agent)
    operation(request) mustEqual expectSuccess
    val filter = new UserAgentCheckFilter()
    val mockNextAction = new MockAction
    val action = filter(mockNextAction)
    action(request)
    mockNextAction.called must beTrue
  }
}

class MockAction extends EssentialAction {

  var called = false

  override def apply(v1: RequestHeader): Iteratee[Array[Byte], Result] = {
    called = true
    null
  }
}
