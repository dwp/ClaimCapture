package models.domain

import language.postfixOps
import models.view.{CachedClaim, Navigation}
import scala.xml.Elem
import xml.DWPCAClaim
import models.DayMonthYear

case class Claim()(sections: List[Section] = List())(implicit navigation: Navigation = Navigation()) extends DigitalForm(sections)(navigation) {

  def copyForm(sections: List[Section])(implicit navigation:Navigation): DigitalForm = this.copy()(sections)(navigation)

  def xml(transactionId:String): Elem = DWPCAClaim.xml(this,transactionId)

  def cacheKey = CachedClaim.key

  def dateOfClaim: Option[DayMonthYear] = questionGroup(ClaimDate) match {
    case Some(c: ClaimDate) => Some(c.dateOfClaim)
    case _ => None
  }

}