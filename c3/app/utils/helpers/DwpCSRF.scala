package utils.helpers

import play.api.mvc.RequestHeader
import play.api.templates.{HtmlFormat, Html}
import play.filters.csrf.CSRF

import scala.util.{Success, Try}

/**
 * CSRF helper for Play calls.
 */
object DwpCSRF {

  /**
   * Render a CSRF form field token
   */
  def formField(implicit request: RequestHeader): Html = {
    val tokenName = Try(CSRF.TokenName) match {
      case Success(s: String) => s
      case _ => "csrfToken"
    }

    val theToken = CSRF.getToken(request).getOrElse(CSRF.Token("testValue1"))

    val tokenValue = HtmlFormat.escape(theToken.value )
    // probably not possible for an attacker to XSS with a CSRF token, but just to be on the safe side...
    Html( s"""<input type="hidden" name="${tokenName}" value="${tokenValue}"/>""")
  }

}
