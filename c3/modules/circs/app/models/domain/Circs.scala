package models.domain

import models.view.{CachedCircs, Navigation}
import scala.xml.Elem
import xml.DWPCoCircs

/**
 * TODO write description
 * @author Jorge Migueis
 *         Date: 11/09/2013
 */
case class Circs()(sections: List[Section] = List())(implicit navigation: Navigation = Navigation()) extends DigitalForm(sections)(navigation) {
  def copyForm(sections: List[Section])(implicit navigation:Navigation): DigitalForm = this.copy()(sections)(navigation)

  def xml(transactionId:String): Elem = DWPCoCircs.xml(this,transactionId)

  def cacheKey = CachedCircs.key
}
