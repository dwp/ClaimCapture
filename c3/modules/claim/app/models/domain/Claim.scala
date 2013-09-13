package models.domain

import language.postfixOps
import models.view.{CachedClaim, Navigation}
import scala.xml.Elem
import xml.DWPCAClaim
import models.DayMonthYear
import com.dwp.carers.s2.xml.validation.{XmlValidatorFactory, XmlValidator}

/**
 * Represents data gathered for a claim. The structure is defined in DigitalForm. Here, we handle specific behaviour
 * as the generation of XML from the data gathered and the key identifying the cache holding instances of this class.
 * @author Jorge Migueis
 */
case class Claim()(sections: List[Section] = List())(implicit navigation: Navigation = Navigation()) extends DigitalForm(sections)(navigation) {

  def copyForm(sections: List[Section])(implicit navigation:Navigation): DigitalForm = this.copy()(sections)(navigation)

  def xml(transactionId:String): Elem = DWPCAClaim.xml(this,transactionId)

  def xmlValidator: XmlValidator = XmlValidatorFactory.buildCaValidator()

  def cacheKey = CachedClaim.key

  def dateOfClaim: Option[DayMonthYear] = questionGroup(ClaimDate) match {
    case Some(c: ClaimDate) => Some(c.dateOfClaim)
    case _ => None
  }

}