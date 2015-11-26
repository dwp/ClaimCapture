package utils.filters


import javax.inject.Inject

import play.api.mvc._

/**
 * A filter that provides additional protection by checking that requests linked to a session always come from same User Agent.
 *
 *
 * @param checkIf Function to call to check whether the request needs to be checked, i.e. User Agent matched expected User Agent.
 * @param setIf Function to call to determine whether the User Agent needs to be cached so we can check it in subsequent request.
 * @param removeIf Function to call to determine whether the User Agent needs to be removed from cache because we shall not need it anymore.
 */
class UserAgentCheckFilter @Inject() (checkIf: (RequestHeader) => Boolean = UserAgentCheckAction.defaultCheckIf,
                                      setIf:(RequestHeader) => Boolean = UserAgentCheckAction.defaultSetIf,
                                      removeIf: (RequestHeader) => Boolean = UserAgentCheckAction.defautRemoveIf) extends EssentialFilter {

  def apply(nextFilter: EssentialAction): EssentialAction = new UserAgentCheckAction(nextFilter, checkIf, setIf, removeIf)
}
