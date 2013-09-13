package models.domain

import models.view.{CachedCircs, Navigation}
import scala.xml.Elem
import xml.DWPCoCircs
import models.DayMonthYear
import com.dwp.carers.s2.xml.validation.{XmlValidatorFactory, XmlValidator}

/**
 * Represents data gathered for change of circumstances. The structure is defined in DigitalForm. Here, we handle specific behaviour
 * as the generation of XML from the data gathered and the key identifying the cache holding instances of this class.
 * @author Jorge Migueis
 */
case class Circs()(sections: List[Section] = List())(implicit navigation: Navigation = Navigation()) extends DigitalForm(sections)(navigation) {
  def copyForm(sections: List[Section])(implicit navigation:Navigation): DigitalForm = this.copy()(sections)(navigation)

  def xml(transactionId:String): Elem = DWPCoCircs.xml(this,transactionId)

  def xmlValidator: XmlValidator = XmlValidatorFactory.buildCocValidator()

  def cacheKey = CachedCircs.key

  def dateOfClaim: Option[DayMonthYear] = None
}
