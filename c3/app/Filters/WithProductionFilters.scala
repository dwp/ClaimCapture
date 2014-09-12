package Filters

import play.api.{Play, GlobalSettings}
import play.api.mvc.{EssentialFilter, EssentialAction, Filters}
import play.filters.csrf.CSRFFilter
import play.api.Play.current

/**
 * Created by jmi on 12/09/2014.
 */
class WithProductionFilters(filters : play.api.mvc.EssentialFilter*) extends GlobalSettings {
  override def doFilter(a : play.api.mvc.EssentialAction) : play.api.mvc.EssentialAction = if (Play.isTest)  removeCSRF(a, filters: _* ) else Filters(super.doFilter(a), filters: _*)

  private def removeCSRF(action: EssentialAction, filters: EssentialFilter*): EssentialAction = {
    noCsrf(action, filters.toList)
  }

  private def noCsrf(action:EssentialAction, filters:List[EssentialFilter]):EssentialAction = {
    val newFilters = filters.filterNot(_.isInstanceOf[CSRFFilter])
    Filters(super.doFilter(action), newFilters: _*)
  }
}
