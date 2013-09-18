package models.domain

import language.postfixOps
import models.view.{CachedClaim, Navigation}
import xml.DWPCAClaim
import models.DayMonthYear
import com.dwp.carers.s2.xml.validation.XmlValidatorFactory

/**
 * Represents data gathered for a claim.
 * The structure is defined in DigitalForm.
 * Here, we handle specific behaviour as the generation of XML from the data gathered and the key identifying the cache holding instances of this class.
 */
case class Claim(override val sections: List[Section] = List(), override val startDigitalFormTime: Long = System.currentTimeMillis())(implicit navigation: Navigation = Navigation()) extends DigitalForm(sections, startDigitalFormTime)(navigation) {

  def copyForm(sections: List[Section])(implicit navigation:Navigation): DigitalForm = copy(sections)(navigation)

  def cacheKey = CachedClaim.key

  def dateOfClaim: Option[DayMonthYear] = questionGroup(ClaimDate) match {
    case Some(c: ClaimDate) => Some(c.dateOfClaim)
    case _ => None
  }

  def xml(transactionId: String) = DWPCAClaim.xml(this, transactionId)

  def xmlValidator = XmlValidatorFactory.buildCaValidator()
}