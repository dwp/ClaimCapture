package utils.filters

import app.ConfigProperties._
import gov.dwp.exceptions.DwpRuntimeException
import models.view.{CachedChangeOfCircs, CachedClaim}
import play.api.{Play, Logger}
import play.api.cache.Cache
import play.api.mvc.{EssentialAction, RequestHeader}
import play.api.Play.current

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


  /**
   * Function called automatically by Play's filters processor. This is where we process the User Agent checks.
   * @param request Request to parse and eventually check.
   * @return If processing is successful it returns the result of the next filter in the chain of filters.
   *         Otherwise it throws an exception to stop processing of the request by the application.
   */
  def apply(request: RequestHeader) = {

    val key = getKeyFromSession(request)

    request match {

      case _ if setIf(request) =>
        if (key.nonEmpty) {
          Logger.debug(s"UserAgentCheckAction set for key ${key}_UA")
          Cache.set(key + "_UA", request.headers.get("User-Agent").getOrElse(""), getProperty("cache.expiry", 3600) * 10)
        } else {
          throw new DwpRuntimeException("Session does not contain key. Cannot save User Agent.")
        }

      case _ if checkIf(request) =>
        if (key.nonEmpty) {
          Logger.debug(s"UserAgentCheckAction check for key ${key}_UA")
          Cache.getAs[String](key + "_UA") match {
            case Some(ua) =>
              val userAgent = request.headers.get("User-Agent").getOrElse("")
              if (ua != userAgent) {
                throw new DwpRuntimeException(s"UserAgent check failed. $userAgent is different from expected $ua.")
              }
              Logger.debug(s"UserAgent $userAgent is equal to expected $ua.")
            case _ => // No claim in cache. Nothing to do. user will get an error because no claim exists. No security risk.
          }
        } else {
         if  (!Play.isTest)
            throw new DwpRuntimeException(s"Session does not contain key. Cannot check User Agent. For ${request.method} url path ${request.path} and agent ${request.headers.get("User-Agent").getOrElse("Unknown agent")}")
        }

      case _ if removeIf(request) =>
        if (key.nonEmpty) {
          Logger.debug(s"UserAgentCheckAction remove for key ${key}_UA")
          Cache.remove(key + "_UA")
        }

      case _ => // Nothing to do really. A request we are not interested in, e.g. access to public assets.
    }
    // Call next filter's action in the chain.
    next(request)
  }

  private def getKeyFromSession(header: RequestHeader) = {
    header.session.get(CachedClaim.key).getOrElse(header.session.get(CachedChangeOfCircs.key).getOrElse(""))
  }

}

/**
 * Object that provides default behaviour for the UserAgentCheckAction class.
 */
object UserAgentCheckAction {

  def defaultCheckIf(header: RequestHeader): Boolean = !RequestSelector.startPage(header) && RequestSelector.toBeChecked(header)

  def defaultSetIf(header: RequestHeader): Boolean = header.method == "POST" && RequestSelector.startPage(header)

  def defautRemoveIf(header: RequestHeader): Boolean = RequestSelector.endPage(header)
}
