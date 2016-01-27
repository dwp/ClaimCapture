package xml.claim

import models.domain._
import scala.xml.NodeSeq
import org.joda.time.DateTime
import xml.XMLComponent

/**
 * Generate the XML presenting the Assisted decisions.
 * @author Jorge Migueis/Peter Whitehead
 */
object AssistedDecision extends XMLComponent {
  val emptyAssistedDecision : NodeSeq = <AssistedDecisions><AssistedDecision><Reason>None</Reason><RecommendedDecision>None</RecommendedDecision></AssistedDecision></AssistedDecisions>
  def xml(claim: Claim) = {
    val isDecisionMade = (assisted: NodeSeq) => assisted != NodeSeq.Empty
    val fnList = Array[(Claim) => NodeSeq](caringHours _, dateOfClaim _, noEEA _, yesEEAGuardYesBenefitsWork _, isInEducation _)
    val assisted = process(isDecisionMade, claim)(fnList)
    if (assisted.length > 0) <AssistedDecisions>{assisted}</AssistedDecisions>
    else emptyAssistedDecision
  }

  // ============ Decision functions ====================

  private def caringHours(claim: Claim): NodeSeq = {
    if (!isOver35Hours(claim)) decisionElement("Not caring 35 hours a week.","Potential disallowance decision,no table")
    else NodeSeq.Empty
  }

  private def dateOfClaim(claim: Claim): NodeSeq = {
    if (isOverThreeMonthsOneDay(claim)) decisionElement("Claim date over 3 months into future.", "Potential disallowance decision,no table")
    else NodeSeq.Empty
  }

  private def noEEA(claim: Claim): NodeSeq = {
    val otherEEAStateOrSwitzerland = claim.questionGroup[OtherEEAStateOrSwitzerland].getOrElse(OtherEEAStateOrSwitzerland())
    (otherEEAStateOrSwitzerland.guardQuestion.answer, claim.questionGroup[Benefits].getOrElse(new Benefits()).benefitsAnswer) match {
      case ("no", Benefits.noneOfTheBenefits) => decisionElement("DP on No QB. Check CIS.", "Potential disallowance decision,show table")
      case ("no", Benefits.afip) => decisionElement("Assign to AFIP officer on CAMLite workflow.", "None,show table")
      case _ => NodeSeq.Empty
    }
  }

  private def yesEEAGuardYesBenefitsWork(claim: Claim): NodeSeq = {
    val otherEEAStateOrSwitzerland = claim.questionGroup[OtherEEAStateOrSwitzerland].getOrElse(OtherEEAStateOrSwitzerland())
    if (otherEEAStateOrSwitzerland.guardQuestion.answer == "yes" &&
      (otherEEAStateOrSwitzerland.guardQuestion.field1.get.answer == "yes" ||
        otherEEAStateOrSwitzerland.guardQuestion.field2.get.answer == "yes"))
      decisionElement("Assign to Exportability in CAMLite workflow.", "None,show table")
    else NodeSeq.Empty
  }

  private def isInEducation(claim: Claim): NodeSeq = {
    val otherEEAStateOrSwitzerland = claim.questionGroup[OtherEEAStateOrSwitzerland].getOrElse(OtherEEAStateOrSwitzerland())
    if (otherEEAStateOrSwitzerland.guardQuestion.answer == "no" &&
      claim.questionGroup[Benefits].getOrElse(new Benefits()).benefitsAnswer != Benefits.afip &&
      claim.questionGroup[YourCourseDetails].getOrElse(new YourCourseDetails()).beenInEducationSinceClaimDate == "yes")
      decisionElement("Send DS790/790B COMB to customer.", "None,show table")
    else NodeSeq.Empty
  }

  private def isOver35Hours(claim: Claim) : Boolean = {
    val hours = claim.questionGroup[MoreAboutTheCare].getOrElse(MoreAboutTheCare())
    hours.spent35HoursCaring.toLowerCase == "yes"
  }

  private def isOverThreeMonthsOneDay(claim: Claim) : Boolean = {
    val claimDateAnswer = claim.questionGroup[ClaimDate].getOrElse(ClaimDate())
    val monthsFuture = DateTime.now().plusMonths(3).plusDays(1)
    val claimDate = new DateTime(claimDateAnswer.dateOfClaim.year.get, claimDateAnswer.dateOfClaim.month.get, claimDateAnswer.dateOfClaim.day.get, 0, 0)
    claimDate.isAfter(monthsFuture)
  }

  private def decisionElement(reason: String, decision:String) = <AssistedDecision><Reason>{reason}</Reason><RecommendedDecision>{decision}</RecommendedDecision></AssistedDecision>

  private def process(decision: NodeSeq => Boolean, claim: Claim)(fns: Array[(Claim) => NodeSeq]) : NodeSeq = {
    for (f <- fns) {
      val result = f(claim)
      if (decision(result)) return result
    }
    NodeSeq.Empty
  }
}
