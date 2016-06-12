package utils.csrf

import gov.dwp.exceptions.DwpRuntimeException
import play.api.Play
import play.api.Play.current
import play.api.http.HeaderNames._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.iteratee._
import play.api.mvc.BodyParsers.parse._
import play.api.mvc._
import utils.csrf.DwpCSRF._

/**
 * An action that provides CSRF protection. Original code comes from Play GitHub, filters plugin.
 * If Play is in test mode then this Action does nothing, it just calls the next action in the chain of actions.
 *
 * @param tokenName The key used to store the token in the Play session.  Defaults to csrfToken.
 * @param cookieName If defined, causes the filter to store the token in a Cookie with this name instead of the session.
 * @param secureCookie If storing the token in a cookie, whether this Cookie should set the secure flag.  Defaults to
 *                     whether the session cookie is configured to be secure.
 * @param createIfNotFound Whether a new CSRF token should be created if it's not found.  Default creates one if it's
 *                         a GET request that accepts HTML.
 * @param tokenProvider A token provider to use.
 * @param next The composed action that is being protected.
 */
class DwpCSRFAction(next: EssentialAction,
                    tokenName: String = CSRFConf.TokenName,
                    cookieName: Option[String] = CSRFConf.CookieName,
                    secureCookie: Boolean = CSRFConf.SecureCookie,
                    createIfFound: (RequestHeader) => Boolean = CSRFConf.defaultCreateIfFound,
                    createIfNotFound: RequestHeader => Boolean = CSRFConf.defaultCreateIfNotFound,
                    tokenProvider: TokenProvider = CSRFConf.defaultTokenProvider) extends EssentialAction {

  import utils.csrf.DwpCSRFAction._

  def apply(request: RequestHeader) = {

    // this function exists purely to aid readability
    def continue = next(request)

    // Only filter unsafe methods and content types
    if (CSRFConf.UnsafeMethods(request.method) && request.contentType.exists(CSRFConf.UnsafeContentTypes)) {

      // On CAOL we do not want CSRF triggered when in test mode.
      if (Play.isTest) {
        continue
      } else {

        // Only proceed with checks if there is an incoming token in the header, otherwise there's no point
        getTokenFromHeader(request, tokenName, cookieName).map { headerToken =>

          // First check if there's a token in the query string or header, if we find one, don't bother handling the body
          getTokenFromQueryString(request, tokenName).map { queryStringToken =>

            if (tokenProvider.compareTokens(headerToken, queryStringToken)) {
              filterLogger.trace("[CSRF] Valid token found in query string")
              continue
            } else {
              filterLogger.trace("[CSRF] Check failed because invalid token found in query string: " + queryStringToken)
              checkFailed("Bad CSRF token found in query String")
            }

          } getOrElse {

            // Check the body
            request.contentType match {
              case Some("application/x-www-form-urlencoded") => checkFormBody(request, headerToken, tokenName, next)
              case Some("multipart/form-data") => checkMultipartBody(request, headerToken, tokenName, next)
              // No way to extract token from text plain body
              case Some("text/plain") => {
                filterLogger.trace("[CSRF] Check failed because text/plain request")
                checkFailed("No CSRF token found for text/plain body")
              }
              // Should never happen.
              case _ => {
                filterLogger.trace(s"[CSRF] Check failed because ${request.contentType.getOrElse("unknown type")} request")
                checkFailed(s"No CSRF token found for ${request.contentType.getOrElse("unknown type")} body")
              }
            }

          }
        } getOrElse {

          filterLogger.trace("[CSRF] Check failed because no token found in headers")
          checkFailed("No CSRF token found in headers")

        }
      }
    } else if ((getTokenFromHeader(request, tokenName, cookieName).isEmpty && createIfNotFound(request)) || (!getTokenFromHeader(request, tokenName, cookieName).isEmpty  && createIfFound(request))) {

      // No token in header and we have to create one if not found, so create a new token
      val newToken = tokenProvider.generateToken

      // The request
      val requestWithNewToken = request.copy(tags = request.tags - Token.RequestTag + (Token.RequestTag -> newToken))

      // Once done, add it to the result
      next(requestWithNewToken).map(result =>
        DwpCSRFAction.addTokenToResponse(tokenName, cookieName, secureCookie, newToken, request, result))

    } else {
      filterLogger.trace("[CSRF] No check necessary")
      next(request)
    }
  }

  // An iteratee that returns a forbidden result saying the CSRF check failed
  private def checkFailed(msg: String): Iteratee[Array[Byte], Result] = throw new DwpRuntimeException(s"Invalid CSRF - ${msg}") //Done(Forbidden(msg))

  private def checkFormBody = checkBody[Map[String, Seq[String]]](tolerantFormUrlEncoded, identity) _

  private def checkMultipartBody = checkBody[MultipartFormData[Unit]](multipartFormData[Unit]({
    case _ => Iteratee.ignore[Array[Byte]].map(_ => MultipartFormData.FilePart("", "", None, ()))
  }), _.dataParts) _

  private def checkBody[T](parser: BodyParser[T], extractor: (T => Map[String, Seq[String]]))(request: RequestHeader, tokenFromHeader: String,
                                                                                              tokenName: String, next: EssentialAction) = {
    // Take up to 100kb of the body
    val firstPartOfBody: Iteratee[Array[Byte], Array[Byte]] =
      Traversable.take[Array[Byte]](CSRFConf.PostBodyBuffer.asInstanceOf[Int]) &>> Iteratee.consume[Array[Byte]]()

    firstPartOfBody.flatMap { bytes: Array[Byte] =>
      // Parse the first 100kb
      val parsedBody = Enumerator(bytes) |>>> parser(request)

      Iteratee.flatten(parsedBody.map { parseResult =>
        val validToken = parseResult.fold(
          // error parsing the body, we couldn't find a valid token
          _ => false,
          // extract the token and verify
          body => (for {
            values <- extractor(body).get(tokenName)
            token <- values.headOption
          } yield tokenProvider.compareTokens(token, tokenFromHeader)).getOrElse(false)
        )

        if (validToken) {
          // Feed the buffered bytes into the next request, and return the iteratee
          filterLogger.trace("[CSRF] Valid token found in body")
          Iteratee.flatten(Enumerator(bytes) |>> next(request))
        } else {
          filterLogger.trace("[CSRF] Check failed because no or invalid token found in body")
          checkFailed("Invalid token found in form body")
        }
      })
    }
  }

}

/**
 * This is the object that does most of the CSRF work. It checks the CSRF tokens received (post action) and add CSRF token to cookies to Gets.
 */
object DwpCSRFAction {

  private[csrf] def getTokenFromHeader(request: RequestHeader, tokenName: String, cookieName: Option[String]) = {
    cookieName.flatMap(cookie => request.cookies.get(cookie).map(_.value))
      .orElse(request.session.get(tokenName))
  }

  private[csrf] def getTokenFromQueryString(request: RequestHeader, tokenName: String) = {
    request.getQueryString(tokenName)
      .orElse(request.headers.get(CSRFConf.HeaderName))
  }

  private[csrf] def checkCsrfBypass(request: RequestHeader) = {
    if (request.headers.get(CSRFConf.HeaderName).exists(_ == CSRFConf.HeaderNoCheck)) {

      // Since injecting arbitrary header values is not possible with a CSRF attack, the presence of this header
      // indicates that this is not a CSRF attack
      filterLogger.trace("[CSRF] Bypassing check because " + CSRFConf.HeaderName + ": " + CSRFConf.HeaderNoCheck + " header found")
      true

    } else if (request.headers.get("X-Requested-With").isDefined) {

      // AJAX requests are not CSRF attacks either because they are restricted to same origin policy
      filterLogger.trace("[CSRF] Bypassing check because X-Requested-With header found")
      true
    } else {
      false
    }
  }

  private[csrf] def addTokenToResponse(tokenName: String, cookieName: Option[String], secureCookie: Boolean,
                                       newToken: String, request: RequestHeader, result: Result) = {
    filterLogger.trace("[CSRF] Adding token to result: " + result)

    cookieName.map {
      // cookie
      name =>
        result.withCookies(Cookie(name, newToken, path = Session.path, domain = Session.domain,
          secure = secureCookie))
    } getOrElse {
      // Get the new session, or the incoming session
      val session = Cookies.fromSetCookieHeader(result.header.headers.get(SET_COOKIE))
        .get(Session.COOKIE_NAME).map(_.value).map(Session.decode)
        .getOrElse(request.session.data)

      val newSession = session + (tokenName -> newToken)
      result.withSession(Session.deserialize(newSession))
    }
  }
}
