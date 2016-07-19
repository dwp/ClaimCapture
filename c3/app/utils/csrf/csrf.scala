package utils.csrf

import gov.dwp.exceptions.DwpRuntimeException
import play.api.Play.current
import play.api._
import play.api.libs.Crypto
import play.api.mvc._

/**
 * List of configuration entries in the configuration files to control CSRF behaviour.
 * See PlayFramework documentation on CSRF https://www.playframework.com/documentation/2.2.1/ScalaCsrf.
 */
private[csrf] object CSRFConf {

  def c = Play.configuration

  def TokenName: String = c.getString("play.filters.csrf.token.name").getOrElse("csrfToken")

  def CookieName: Option[String] = c.getString("play.filters.csrf.cookie.name")

  def SecureCookie: Boolean = c.getBoolean("play.filters.csrf.cookie.secure").getOrElse(Session.secure)

  def PostBodyBuffer: Long = c.getBytes("play.filters.csrf.body.bufferSize").getOrElse(102400L)

  def SignTokens: Boolean = c.getBoolean("play.filters.csrf.sign.tokens").getOrElse(true)

  val UnsafeMethods = Set("POST")
  val UnsafeContentTypes = Set("application/x-www-form-urlencoded", "text/plain", "multipart/form-data")

  val HeaderName = "Csrf-Token"
  val HeaderNoCheck = "nocheck"

  def defaultCreateIfNotFound(request: RequestHeader) = {
    // If the request isn't accepting HTML, then it won't be rendering a form, so there's no point in generating a
    // CSRF token for it.
    request.method == "GET" && (request.accepts("text/html") || request.accepts("application/xml+xhtml"))
  }

  def defaultCreateIfFound(request: RequestHeader) = false

  /**
   * Provides the token provider based on the application configuration (csrf.sign.tokens).
   * @return The token provider as defined by csrf.sign.tokens property. See [[DwpCSRF.SignedTokenProvider]] and [[DwpCSRF.UnsignedTokenProvider]].
   */
  def defaultTokenProvider = {
    if (SignTokens) {
      DwpCSRF.SignedTokenProvider
    } else {
      DwpCSRF.UnsignedTokenProvider
    }
  }
}

/**
 * Original code comes from Play GitHub, filters plugin.
 * This object provided utility functions to read a CSRF token from a request, or generate a new one (signed or not signed).
 */
object DwpCSRF {

  private[csrf] val filterLogger = play.api.Logger("play.filters")

  /**
   * A CSRF token
   */
  case class Token(value: String)

  object Token {
    val RequestTag = "CSRF_TOKEN"

    implicit def getToken(implicit request: RequestHeader): Token = {
      DwpCSRF.getToken(request).getOrElse({
        throw new DwpRuntimeException("Missing CSRF Token")
      })
    }
  }

  // Allows the template helper to access it
  def TokenName = CSRFConf.TokenName

  import utils.csrf.CSRFConf._

  /**
   * Extract token from current request
   */
  def getToken(request: RequestHeader): Option[Token] = {
    // First check the tags, this is where tokens are added if it's added to the current request
    val token = request.tags.get(Token.RequestTag)
      // Check cookie if cookie name is defined
      .orElse(CookieName.flatMap(n => request.cookies.get(n).map(_.value)))
      // Check session
      .orElse(request.session.get(TokenName))
    if (SignTokens) {
      // Extract the signed token, and then resign it. This makes the token random per request, preventing the BREACH
      // vulnerability
      token.flatMap(Crypto.extractSignedToken)
        .map(token => Token(Crypto.signToken(token)))
    } else {
      token.map(Token.apply)
    }
  }

  /**
   * A token provider, for generating and comparing tokens.
   *
   * This abstraction allows the use of randomised tokens.
   */
  trait TokenProvider {
    /** Generate a token */
    def generateToken: String

    /** Compare two tokens */
    def compareTokens(tokenA: String, tokenB: String): Boolean
  }

  /**
   * Provider used to generate s signed (encrypted) CSRF token.
   */
  object SignedTokenProvider extends TokenProvider {
    def generateToken = Crypto.generateSignedToken

    def compareTokens(tokenA: String, tokenB: String) = Crypto.compareSignedTokens(tokenA, tokenB)
  }

  /**
   * Provided  used to generate an unsigned (unencrypted) CSRF token.
   */
  object UnsignedTokenProvider extends TokenProvider {
    def generateToken = Crypto.generateToken

    def compareTokens(tokenA: String, tokenB: String) = Crypto.constantTimeEquals(tokenA, tokenB)
  }

}
