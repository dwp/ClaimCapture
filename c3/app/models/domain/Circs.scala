package models.domain

import models.view.{CachedCircs, Navigation}
import xml.DWPCoCircs
import models.DayMonthYear
import com.dwp.carers.s2.xml.validation.XmlValidatorFactory

/**
 * Represents data gathered for change of circumstances.
 * The structure is defined in DigitalForm.
 * Here, we handle specific behaviour as the generation of XML from the data gathered and the key identifying the cache holding instances of this class.
 */
case class Circs(override val sections: List[Section] = List(), override val startDigitalFormTime: Long = System.currentTimeMillis())(implicit navigation: Navigation = Navigation()) extends DigitalForm(sections, startDigitalFormTime)(navigation) {

  def copyForm(sections: List[Section])(implicit navigation: Navigation): DigitalForm = copy(sections)(navigation)

  def cacheKey = CachedCircs.key

  def dateOfClaim: Option[DayMonthYear] = None

  def xml(transactionId: String) = DWPCoCircs.xml(this, transactionId)

  def xmlValidator = XmlValidatorFactory.buildCocValidator()

  def honeyPot: Boolean = {
    def checkDeclaration: Boolean = {
      questionGroup(CircumstancesDeclaration) match {
        case Some(q) => {
          val h = q.asInstanceOf[CircumstancesDeclaration]
          if (h.obtainInfoAgreement == "yes") {
            h.obtainInfoWhy match {
              case Some(f) => true // Bot given field howOftenPersonal was not visible.
              case _ => false
            }
          }
          else false
        }
        case _ => false
      }
    }

    checkDeclaration
  }
}