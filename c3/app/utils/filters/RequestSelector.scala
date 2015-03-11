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
    !headerString.matches(".*assets.*") && !endPage(request) && !headerString.matches(".*error.*")
  }

  /**
   * Is this a start page of the system?
   * @param request Request to parse.
   * @return true if request corresponds to a start page, false otherwise.
   */
  def startPage(request:RequestHeader) = {
    val headerString = request.path
    headerString.matches(".*circumstances.identification.*") || headerString.matches(".*allowance.benefits.*")
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
