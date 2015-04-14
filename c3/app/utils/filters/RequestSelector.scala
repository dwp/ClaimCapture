package utils.filters

import play.api.mvc.RequestHeader

/**
 * Functions used to filters requests/pages. Used by filters and actions.
 */
object RequestSelector {

  /**
   * Does a request need to get checked?
   * @param request Request to parse.
   * @return true if security checks need to be applied, false otherwise.
   */
  def toBeChecked(request:RequestHeader) = {
    val headerString = request.path
    !headerString.matches(".*assets.*") && !headerString.matches(".*images.*") && !endPage(request) && !headerString.matches(".*error.*") &&
      !headerString.matches(".*back-button.*") && !headerString.matches("/report/.*") && !headerString.matches("/favicon.ico")
  }

  /**
   * Is this a start page of the system?
   * @param request Request to parse.
   * @return true if request corresponds to a start page, false otherwise.
   */
  def startPage(request:RequestHeader) = {
    val headerString = request.path
    headerString.matches(".*circumstances.identification.*") || headerString.matches(".*allowance.benefits.*") || headerString.matches(".*change-language.*")
  }

  /**
   * Is this a Channel shift related page
   * @param request Request to parse
   * @return true if request corresponds to a channel shift redirection.
   */
  def channelShiftPage(request:RequestHeader) = {
    val headerString = request.path
    headerString.matches(".*CS2015.*")
  }

  /**
   * Is this an end page of the system?
   * @param request Request to parse.
   * @return true if request corresponds to an end page, false otherwise.
   */
  def endPage(request:RequestHeader) = {
    val headerString = request.path
    headerString.matches(".*thankyou.*") || headerString.matches(".*timeout.*")
  }

}
