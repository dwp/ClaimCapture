package utils.filters

import gov.dwp.exceptions.DwpRuntimeException
import models.view.cache.CacheHandling
import models.view.{ClaimHandling, CachedChangeOfCircs, CachedClaim}
import play.api.http.HttpVerbs
import play.api.{Play, Logger}
import play.api.cache.CacheApi
import play.api.mvc.{EssentialAction, RequestHeader}
import play.api.Play.current
import scala.concurrent.duration._

import scala.concurrent.duration.Duration

case class UserAgentCheckException(message:String) extends DwpRuntimeException(message)

/**
 * Action used by security [[utils.filters.UserAgentCheckFilter]] to check User Agent in request matches User Agent used to start claim/cofcs.
 * @param next The next action that will be called if user agent check is successful.
 * @param checkIf Function to call to check whether the request needs to be checked, i.e. User Agent matched expected User Agent.
 * @param setIf Function to call to determine whether the User Agent needs to be cached so we can check it in subsequent request.
 * @param removeIf Function to call to determine whether the User Agent needs to be removed from cache because we shall not need it anymore.
 */
class UserAgentCheckAction(next: EssentialAction, checkIf: (RequestHeader) => Boolean,
                           setIf: (RequestHeader) => Boolean,
                           removeIf: (RequestHeader) => Boolean) extends EssentialAction {

  val cache = current.injector.instanceOf[CacheApi]

  private def renameThread(uuid : String): Unit = if(!uuid.isEmpty)Thread.currentThread().setName(uuid)

  /**
   * Function called automatically by Play's filters processor. This is where we process the User Agent checks.
   * @param request Request to parse and eventually check.
   * @return If processing is successful it returns the result of the next filter in the chain of filters.
   *         Otherwise it throws an exception to stop processing of the request by the application.
   */
  def apply(request: RequestHeader) = {

    val key = getKeyFromSession(request)
    val UAKey = s"${CacheHandling.claimCacheNamespace}${key}_UA"

    request match {

      case _ if setIf(request) =>
        if (key.nonEmpty) {
          Logger.debug(s"UserAgentCheckAction set for key ${UAKey}")
          cache.set(UAKey, request.headers.get("User-Agent").getOrElse(""), Duration(CacheHandling.expiration + 100, SECONDS))
        } else {
          throw UserAgentCheckException("Session does not contain key. Cannot save User Agent.")
        }

      case _ if checkIf(request) =>
        if (key.nonEmpty) {
          Logger.debug(s"UserAgentCheckAction check for key ${UAKey}")
          cache.get[String](UAKey) match {
            case Some(ua) =>
              val userAgent = request.headers.get("User-Agent").getOrElse("")
              if (ua != userAgent) {
                throw UserAgentCheckException(s"UserAgent check failed. $userAgent is different from expected $ua.")
              }
              // to update expiration time and avoid issues when restarting caches.
              if (request.method == HttpVerbs.GET) cache.set("default"+UAKey, request.headers.get("User-Agent").getOrElse(""), Duration(CacheHandling.expiration + 100, SECONDS))


            case None if (cache.get(key).isDefined) =>
              Logger.warn(s"Lost User Agent from cache while claim still in cache? For ${request.method} url path ${request.path} and key $UAKey" )

            case _ =>
            // No claim in cache. Nothing to do. user will get an error because no claim exists. No security risk.
          }
        } else {
          if (!Play.isTest)
            throw UserAgentCheckException(s"Session does not contain key. Cannot check User Agent. For ${request.method} url path ${request.path} and agent ${request.headers.get("User-Agent").getOrElse("Unknown agent")}. Cookies present ${request.cookies.isEmpty}")
        }

      case _ if removeIf(request) =>
        if (key.nonEmpty) {
          Logger.debug(s"UserAgentCheckAction remove for key ${UAKey}")
          cache.remove(UAKey)
        }

      case _ => // Nothing to do really. A request we are not interested in, e.g. access to public assets.
    }
    // Call next filter's action in the chain.
    next(request)
  }

  private def getKeyFromSession(header: RequestHeader) = {
    renameThread(header.session.get(CachedClaim.key).getOrElse(header.session.get(CachedChangeOfCircs.key).getOrElse("")))
    header.session.get(CachedClaim.key).getOrElse(header.session.get(CachedChangeOfCircs.key).getOrElse(""))
  }
}

/**
 * Object that provides default behaviour for the UserAgentCheckAction class.
 */
object UserAgentCheckAction {

  def defaultCheckIf(header: RequestHeader): Boolean = (!RequestSelector.startPage(header)
    && !RequestSelector.channelShiftPage(header)
    && RequestSelector.toBeChecked(header)
    && (header.cookies.get(ClaimHandling.applicationFinished) match {
    case Some(c) => c.value == "false"
    case _ => true
  }))

  def defaultSetIf(header: RequestHeader): Boolean = header.method ==  HttpVerbs.POST && RequestSelector.startPage(header)

  def defautRemoveIf(header: RequestHeader): Boolean = RequestSelector.endPage(header)

}
