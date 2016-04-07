package xml.claim

import models.domain._
import org.joda.time.DateTime
import xml.XMLComponent

import scala.xml.NodeSeq

/**
 * Generate the XML presenting the Assisted decisions.
 * @author Jorge Migueis/Peter Whitehead
 */
object AssistedDecision extends XMLComponent {
  val emptyAssistedDecisionDetails : AssistedDecisionDetails = new AssistedDecisionDetails

  def createAssistedDecisionDetails(claim: Claim): Claim = {
    val isDecisionMade = (assisted: AssistedDecisionDetails) => assisted.reason != "None"
    val fnList = Array[(Claim) => AssistedDecisionDetails](dateOfClaim _, caringHours _, isInReceiptOfBenefit _, isAFIP _, yesEEAGuardWork _, isInEducation _, isHappyPath _)
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

  private def isAFIP(claim: Claim): AssistedDecisionDetails = {
    if (claim.questionGroup[Benefits].getOrElse(new Benefits()).benefitsAnswer == Benefits.afip) decisionModel("Assign to AFIP officer on CAMLite workflow.", "None,show table")
    else emptyAssistedDecisionDetails
  }

  private def isInReceiptOfBenefit(claim: Claim): AssistedDecisionDetails = {
    if (claim.questionGroup[Benefits].getOrElse(new Benefits()).benefitsAnswer == Benefits.noneOfTheBenefits) decisionModel("DP on No QB. Check CIS.", "Potential disallowance decision,show table")
    else emptyAssistedDecisionDetails
  }

  private def dateOfClaim(claim: Claim): AssistedDecisionDetails = {
    if (isOverThreeMonthsOneDay(claim)) decisionModel("Claim date over 3 months into future.", "Potential disallowance decision,no table")
    else emptyAssistedDecisionDetails
  }

  private def yesEEAGuardWork(claim: Claim): AssistedDecisionDetails = {
    if (isEEA(claim))
        decisionModel("Assign to Exportability in CAMLite workflow.", "None,show table")
    else emptyAssistedDecisionDetails
  }

  private def isInEducation(claim: Claim): AssistedDecisionDetails = {
    if (claim.questionGroup[YourCourseDetails].getOrElse(new YourCourseDetails()).beenInEducationSinceClaimDate == "yes")
      decisionModel("Send DS790/790B COMB to customer.", "None,show table")
    else emptyAssistedDecisionDetails
  }

  def checkBenefits(benefitsAnswer: String) = {
    benefitsAnswer match {
      case Benefits.aa | Benefits.pip | Benefits.dla | Benefits.caa => true
      case _ => false
    }
  }

  private def isHappyPath(claim: Claim): AssistedDecisionDetails = {
    //val aboutYourMoney = claim.questionGroup[AboutOtherMoney].getOrElse(AboutOtherMoney())
    val employment = claim.questionGroup[YourIncomes].getOrElse(models.domain.YourIncomes())
    val nationalityAndResidency = claim.questionGroup[NationalityAndResidency].getOrElse(NationalityAndResidency(nationality = "British"))
      (checkBenefits(claim.questionGroup[Benefits].getOrElse(Benefits()).benefitsAnswer),
        nationalityAndResidency.nationality,
        nationalityAndResidency.resideInUK.answer,
        claim.questionGroup[AbroadForMoreThan52Weeks].getOrElse(AbroadForMoreThan52Weeks()).anyTrips,
        isEEA(claim),
        isOver35Hours(claim),
        claim.questionGroup[BreaksInCare].getOrElse(BreaksInCare()).hasBreaks,
        claim.questionGroup[YourCourseDetails].getOrElse(YourCourseDetails()).beenInEducationSinceClaimDate,
        employment.beenEmployedSince6MonthsBeforeClaim,
        employment.beenSelfEmployedSince1WeekBeforeClaim,
//        aboutYourMoney.anyPaymentsSinceClaimDate.answer,
//        aboutYourMoney.statutorySickPay.answer,
//        aboutYourMoney.otherStatutoryPay.answer,
        claim.questionGroup[HowWePayYou].getOrElse(HowWePayYou()).likeToBePaid,
        claim.questionGroup[AdditionalInfo].getOrElse(AdditionalInfo()).anythingElse.answer
       ) match {
        case (true,
              NationalityAndResidency.british | NationalityAndResidency.britishIrish,
              "yes", //resideInUK
              "no",  //any trips abroad
              false,  //EEA
              true,  //over 35 hours
              false,  //has any breaks in care
              "no",  //been in education
              "no",  //employed
              "no",  //self employed
//              "no",  //any payments
//              "no",  //SSP
//              "no",  //other payments
              "yes", //bank account
              "no"   //additional info
              ) => decisionModel("Check CIS for benefits.", "Potential award,show table")
        case _ => emptyAssistedDecisionDetails
      }
  }

  private def isOver35Hours(claim: Claim) : Boolean = {
    val hours = claim.questionGroup[MoreAboutTheCare].getOrElse(MoreAboutTheCare())
    hours.spent35HoursCaring.toLowerCase == "yes"
  }

  private def isEEA(claim: Claim) : Boolean = {
    val otherEEAStateOrSwitzerland = claim.questionGroup[OtherEEAStateOrSwitzerland].getOrElse(OtherEEAStateOrSwitzerland())
    if (otherEEAStateOrSwitzerland.guardQuestion.answer == "yes" &&
      (otherEEAStateOrSwitzerland.guardQuestion.field1.get.answer == "yes" ||
        otherEEAStateOrSwitzerland.guardQuestion.field2.get.answer == "yes"))
      true
    else false
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

  def fromXml(xml: NodeSeq, claim: Claim) : Claim = {
    val decisions = (xml \\ "AssistedDecisions" \ "AssistedDecision")
    val assistedDecisionDetails = AssistedDecisionDetails(reason = (decisions \ "Reason").text, recommendation = (decisions \ "RecommendedDecision").text)
    claim.update(assistedDecisionDetails)
  }
}
