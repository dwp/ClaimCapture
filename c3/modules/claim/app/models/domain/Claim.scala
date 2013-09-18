package models.domain

import language.postfixOps
import models.view.{CachedClaim, Navigation}
import xml.DWPCAClaim
import models.DayMonthYear
import com.dwp.carers.s2.xml.validation.XmlValidatorFactory
import app.PensionPaymentFrequency._

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

  def honeyPot: Boolean = {
    (questionGroup(ChildcareExpenses) match {
      case Some(q) => {
        val h = q.asInstanceOf[ChildcareExpenses]
        (h.howOftenPayChildCare.frequency != Other && h.howOftenPayChildCare.other.isDefined)
      }
      case _ => false
    }) ||
      (questionGroup(PersonYouCareForExpenses) match {
        case Some(q) => {
          val h = q.asInstanceOf[PersonYouCareForExpenses]
          (h.howOftenPayCare.frequency != Other && h.howOftenPayCare.other.isDefined)
        }
        case _ => false
      }) ||
      (questionGroup(ChildcareExpensesWhileAtWork) match {
        case Some(q) => {
          val h = q.asInstanceOf[ChildcareExpensesWhileAtWork]
          (h.howOftenPayChildCare.frequency != Other && h.howOftenPayChildCare.other.isDefined)
        }
        case _ => false
      }) ||
      (questionGroup(ExpensesWhileAtWork) match {
        case Some(q) => {
          val h = q.asInstanceOf[ExpensesWhileAtWork]
          (h.howOftenPayExpenses.frequency != Other && h.howOftenPayExpenses.other.isDefined)
        }
        case _ => false
      })
  }
}