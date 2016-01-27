package xml.claim

import models.domain._
import org.joda.time.DateTime
import xml.XMLComponent

/**
 * Generate the XML presenting the Assisted decisions.
 * @author Jorge Migueis/Peter Whitehead
 */
object AssistedDecision extends XMLComponent {
  val emptyAssistedDecisionDetails : AssistedDecisionDetails = new AssistedDecisionDetails

  def createAssistedDecisionDetails(claim: Claim): Claim = {
    val isDecisionMade = (assisted: AssistedDecisionDetails) => assisted.reason != "None"
    val fnList = Array[(Claim) => AssistedDecisionDetails](caringHours _, dateOfClaim _, noEEA _, yesEEAGuardYesBenefitsWork _, isInEducation _)
    claim.update(process(isDecisionMade, claim)(fnList))
  }

  def xml(claim: Claim) = {
    decisionElement(claim.questionGroup[AssistedDecisionDetails].getOrElse(new AssistedDecisionDetails))
  }

  // ============ Decision functions ====================

  private def caringHours(claim: Claim): AssistedDecisionDetails = {
    if (!isOver35Hours(claim)) decisionModel("Not caring 35 hours a week.","Potential disallowance decision,no table")
    else emptyAssistedDecisionDetails
  }

  private def dateOfClaim(claim: Claim): AssistedDecisionDetails = {
    if (isOverThreeMonthsOneDay(claim)) decisionModel("Claim date over 3 months into future.", "Potential disallowance decision,no table")
    else emptyAssistedDecisionDetails
  }

  private def noEEA(claim: Claim): AssistedDecisionDetails = {
    val otherEEAStateOrSwitzerland = claim.questionGroup[OtherEEAStateOrSwitzerland].getOrElse(OtherEEAStateOrSwitzerland())
    (otherEEAStateOrSwitzerland.guardQuestion.answer, claim.questionGroup[Benefits].getOrElse(new Benefits()).benefitsAnswer) match {
      case ("no", Benefits.noneOfTheBenefits) => decisionModel("DP on No QB. Check CIS.", "Potential disallowance decision,show table")
      case ("no", Benefits.afip) => decisionModel("Assign to AFIP officer on CAMLite workflow.", "None,show table")
      case _ => emptyAssistedDecisionDetails
    }
  }

  private def yesEEAGuardYesBenefitsWork(claim: Claim): AssistedDecisionDetails = {
    val otherEEAStateOrSwitzerland = claim.questionGroup[OtherEEAStateOrSwitzerland].getOrElse(OtherEEAStateOrSwitzerland())
    if (otherEEAStateOrSwitzerland.guardQuestion.answer == "yes" &&
      (otherEEAStateOrSwitzerland.guardQuestion.field1.get.answer == "yes" ||
        otherEEAStateOrSwitzerland.guardQuestion.field2.get.answer == "yes"))
      decisionModel("Assign to Exportability in CAMLite workflow.", "None,show table")
    else emptyAssistedDecisionDetails
  }

  private def isInEducation(claim: Claim): AssistedDecisionDetails = {
    val otherEEAStateOrSwitzerland = claim.questionGroup[OtherEEAStateOrSwitzerland].getOrElse(OtherEEAStateOrSwitzerland())
    if (otherEEAStateOrSwitzerland.guardQuestion.answer == "no" &&
      claim.questionGroup[Benefits].getOrElse(new Benefits()).benefitsAnswer != Benefits.afip &&
      claim.questionGroup[YourCourseDetails].getOrElse(new YourCourseDetails()).beenInEducationSinceClaimDate == "yes")
      decisionModel("Send DS790/790B COMB to customer.", "None,show table")
    else emptyAssistedDecisionDetails
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

  private def decisionElement(assistedDecision: AssistedDecisionDetails) = <AssistedDecisions><AssistedDecision><Reason>{assistedDecision.reason}</Reason><RecommendedDecision>{assistedDecision.recommendation}</RecommendedDecision></AssistedDecision></AssistedDecisions>

  private def decisionModel(reason: String, decision:String) : AssistedDecisionDetails = new AssistedDecisionDetails(reason, decision)

  private def process(decision: AssistedDecisionDetails => Boolean, claim: Claim)(fns: Array[(Claim) => AssistedDecisionDetails]) : AssistedDecisionDetails = {
    for (f <- fns) {
      val result = f(claim)
      if (decision(result)) return result
    }
    emptyAssistedDecisionDetails
  }
}
