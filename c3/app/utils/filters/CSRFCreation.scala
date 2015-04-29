package utils.filters

import play.api.http.HttpVerbs
import play.api.mvc.RequestHeader


object CSRFCreation {

  /**
   * We may need to generate a new token on a Get request for pages that need to be checked.
   */
  def createIfNotFound(request:RequestHeader): Boolean = {
    request.method == HttpVerbs.GET && (request.accepts("text/html") || request.accepts("application/xml+xhtml")) && RequestSelector.toBeChecked(request)
  }

  /**
   * We do want to force generation of new CSRF when on a start page. We discard CSRF that could be from previous visit.
   */
  def createIfFound(request:RequestHeader): Boolean = request.method == "GET" && RequestSelector.startPage(request)
}
