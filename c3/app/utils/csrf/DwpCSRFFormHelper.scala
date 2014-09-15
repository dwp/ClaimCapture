package utils.csrf

import play.api.mvc.RequestHeader
import play.api.templates.{Html, HtmlFormat}

import scala.util.{Success, Try}

/**
 * CSRF helper for Play templates. If no token is available (CSRF filter is deactivated in test mode) then we put a fix value.
 * If CSRF filter is active then the CSRF check will fail.
 */
object DwpCSRFFormHelper {

  /**
   * Render a CSRF form field token
   */
  def formField(implicit request: RequestHeader): Html = {
    val tokenName = Try(DwpCSRF.TokenName) match {
      case Success(s: String) => s
      case _ => "csrfToken"
    }

    val theToken = DwpCSRF.getToken(request).getOrElse(DwpCSRF.Token("testValue1"))

    val tokenValue = HtmlFormat.escape(theToken.value)
    // probably not possible for an attacker to XSS with a CSRF token, but just to be on the safe side...
    Html( s"""<input type="hidden" name="${tokenName}" value="${tokenValue}"/>""")
  }

}
