package xml.claim

import app.ConfigProperties._
import controllers.mappings.Mappings
import models.DayMonthYear
import models.domain._
import org.joda.time.format.DateTimeFormat
import org.joda.time.{Months, DateTime}
import play.api.Logger
import xml.XMLComponent

import scala.xml.NodeSeq

/**
 * Generate the XML presenting the Assisted decisions.
 *
 * @author Jorge Migueis/Peter Whitehead
 */
object AssistedDecision extends XMLComponent {
  val FIFTEENYEARS9MONTHS = 15 * 12 + 9
  val noDecisionSoFar: AssistedDecisionDetails = new AssistedDecisionDetails
  val blankDecisionShowTable = decisionModel("None", "None,show table")

  def createAssistedDecisionDetails(claim: Claim): Claim = {
    val isDecisionMade = (assisted: AssistedDecisionDetails) => assisted.reason != ""
    val fnList = Array[(Claim) => AssistedDecisionDetails](isTooYoung _, dateOfClaim _, caringHours _, isInReceiptOfBenefit _, isAFIP _, yesEEAGuardWork _, isInResidency _, isInEducation _, isBlankShowTable _, isTooOld _, defaultDecision _)

    val decision = process(isDecisionMade, claim)(fnList)
    claim.update(decision)
  }

  def xml(claim: Claim) = {
    decisionElement(claim.questionGroup[AssistedDecisionDetails].getOrElse(new AssistedDecisionDetails))
  }

  // ============ Decision functions ====================
  private def isTooYoung(claim: Claim): AssistedDecisionDetails = {
    val submitDate = DateTime.now
    val yourDetails = claim.questionGroup[YourDetails].getOrElse(YourDetails())
    yourDetails.dateOfBirth match {
      case DayMonthYear(None, None, None, None, None) => noDecisionSoFar
      case _ => {
        val dateOfBirthDate = DateTime.parse(yourDetails.dateOfBirth.`dd-MM-yyyy`, DateTimeFormat.forPattern("dd-MM-yyyy"))
        val monthsOld = Months.monthsBetween(dateOfBirthDate.withTimeAtStartOfDay(), submitDate.withTimeAtStartOfDay())
        if (monthsOld.getMonths < FIFTEENYEARS9MONTHS)
          decisionModel("Customer does not turn 16 in next 3 months. Send Proforma 491 to customer.", "Potential disallowance decision,no table")
        else
          noDecisionSoFar
      }
    }
  }

  private def dateOfClaim(claim: Claim): AssistedDecisionDetails = {
    if (isOverThreeMonthsOneDay(claim)) decisionModel("Claim date over 3 months into future.", "Potential disallowance decision,no table")
    else noDecisionSoFar
  }

  private def caringHours(claim: Claim): AssistedDecisionDetails = {
    if (!isOver35Hours(claim)) decisionModel("Not caring 35 hours a week.", "Potential disallowance decision,no table")
    else noDecisionSoFar
  }

  def checkBenefits(benefitsAnswer: String) = {
    benefitsAnswer match {
      case Benefits.aa | Benefits.pip | Benefits.dla | Benefits.caa => true
      case _ => false
    }
  }

  private def isInReceiptOfBenefit(claim: Claim): AssistedDecisionDetails = {
    if (claim.questionGroup[Benefits].getOrElse(new Benefits()).benefitsAnswer == Benefits.noneOfTheBenefits) decisionModel("DP on No QB. Check CIS.", "Potential disallowance decision,show table")
    else noDecisionSoFar
  }

  private def isAFIP(claim: Claim): AssistedDecisionDetails = {
    if (claim.questionGroup[Benefits].getOrElse(new Benefits()).benefitsAnswer == Benefits.afip) decisionModel("Assign to AFIP officer on CAMLite workflow.", "None,show table")
    else noDecisionSoFar
  }

  private def yesEEAGuardWork(claim: Claim): AssistedDecisionDetails = {
    val paymentsFromAbroad = claim.questionGroup[PaymentsFromAbroad].getOrElse(PaymentsFromAbroad())
    if (paymentsFromAbroad.guardQuestion.answer == "yes" && paymentsFromAbroad.guardQuestion.field1.get.answer == "yes")
      decisionModel("Assign to Exportability in CAMLite workflow.", "None,show table")
    else if (paymentsFromAbroad.guardQuestion.answer == "yes" && paymentsFromAbroad.guardQuestion.field2.get.answer == "yes")
      decisionModel("Assign to Exportability in CAMLite workflow.", "None,show table")
    else noDecisionSoFar
  }

  private def isInResidency(claim: Claim): AssistedDecisionDetails = {
    val residency = claim.questionGroup[NationalityAndResidency].getOrElse(NationalityAndResidency(nationality = "British"))
    (residency.alwaysLivedInUK, residency.liveInUKNow, residency.arrivedInUK) match {
      case ("no", Some("no"), _) => decisionModel("Assign to Exportability in CAMLite workflow.", "None,show table")
      case ("no", _, Some("less")) => decisionModel("Assign to Exportability in CAMLite workflow.", "None,show table")
      case _ => noDecisionSoFar
    }
  }

  private def isInEducation(claim: Claim): AssistedDecisionDetails = {
    if (claim.questionGroup[YourCourseDetails].getOrElse(new YourCourseDetails()).beenInEducationSinceClaimDate == "yes")
      decisionModel("Send DS790/790B COMB to customer.", "None,show table")
    else noDecisionSoFar
  }

  // If no date of claim then its test with missing data so default to happy
  private def lessThan65YearsOldAtClaimDate(claim: Claim) = {
    val yourDetails = claim.questionGroup[YourDetails].getOrElse(YourDetails())
    claim.dateOfClaim match {
      case Some(dmy) => yourDetails.dateOfBirth.yearsDiffWith(dmy) < getIntProperty("age.hide.paydetails")
      case _ => true
    }
  }

  private def isBlankShowTable(claim: Claim): AssistedDecisionDetails = {
    val yourIncomes = claim.questionGroup[YourIncomes].getOrElse(models.domain.YourIncomes())
    val nationalityAndResidency = claim.questionGroup[NationalityAndResidency].getOrElse(NationalityAndResidency(nationality = "British"))
    if (nationalityAndResidency.trip52weeks == "yes") {
      Logger.info(s"AssistedDecision trip52weeks means emptyDecision")
      blankDecisionShowTable
    }
    else if (claim.questionGroup[BreaksInCare].getOrElse(BreaksInCare()).hasBreaks) {
      Logger.info(s"AssistedDecision breaksInCare means emptyDecision")
      blankDecisionShowTable
    }
    else if (claim.questionGroup[YourCourseDetails].getOrElse(YourCourseDetails()).beenInEducationSinceClaimDate == "yes") {
      Logger.info(s"AssistedDecision Education means emptyDecision")
      blankDecisionShowTable
    }
    else if (yourIncomes.beenEmployedSince6MonthsBeforeClaim == "yes") {
      Logger.info(s"AssistedDecision Employed means emptyDecision")
      blankDecisionShowTable
    }
    else if (yourIncomes.beenSelfEmployedSince1WeekBeforeClaim == "yes") {
      Logger.info(s"AssistedDecision SelfEmployed means emptyDecision")
      blankDecisionShowTable
    }
    else if (
      yourIncomes.yourIncome_sickpay == Mappings.someTrue
        || yourIncomes.yourIncome_patmatadoppay == Mappings.someTrue
        || yourIncomes.yourIncome_fostering == Mappings.someTrue
        || yourIncomes.yourIncome_directpay == Mappings.someTrue
        || yourIncomes.yourIncome_rentalincome == Mappings.someTrue
        || yourIncomes.yourIncome_anyother == Mappings.someTrue) {
      Logger.info(s"AssistedDecision Income means emptyDecision")
      blankDecisionShowTable
    }
    else if (claim.questionGroup[HowWePayYou].getOrElse(HowWePayYou()).likeToBePaid == "no" && lessThan65YearsOldAtClaimDate(claim)) {
      Logger.info(s"AssistedDecision under65 no LikeToBePaid means emptyDecision")
      blankDecisionShowTable
    }
    else if (claim.questionGroup[AdditionalInfo].getOrElse(AdditionalInfo()).anythingElse.answer == "yes") {
      Logger.info(s"AssistedDecision AdditionalInfo means emptyDecision")
      blankDecisionShowTable
    }
    else{
      noDecisionSoFar
    }
  }

  private def isTooOld(claim: Claim): AssistedDecisionDetails = {
    if (lessThan65YearsOldAtClaimDate(claim)) noDecisionSoFar
    else decisionModel("Check CIS for benefits. Send Pro517 if relevant.", "Potential underlying entitlement,show table")
  }

  private def defaultDecision(claim: Claim): AssistedDecisionDetails = {
    Logger.info(s"AssistedDecision default happy path means check CIS")
    decisionModel("Check CIS for benefits. Send Pro517 if relevant.", "Potential award,show table")
  }

  private def isOver35Hours(claim: Claim): Boolean = {
    val hours = claim.questionGroup[MoreAboutTheCare].getOrElse(MoreAboutTheCare())
    hours.spent35HoursCaring.getOrElse("").toLowerCase == "yes"
  }

  private def isEEA(claim: Claim): Boolean = {
    val paymentsFromAbroad = claim.questionGroup[PaymentsFromAbroad].getOrElse(PaymentsFromAbroad())
    if (paymentsFromAbroad.guardQuestion.answer == "yes" &&
      (paymentsFromAbroad.guardQuestion.field1.get.answer == "yes" ||
        paymentsFromAbroad.guardQuestion.field2.get.answer == "yes"))
      true
    else false
  }

  private def isOverThreeMonthsOneDay(claim: Claim): Boolean = {
    val claimDateAnswer = claim.questionGroup[ClaimDate].getOrElse(ClaimDate())
    val monthsFuture = DateTime.now().plusMonths(3).plusDays(1)
    val claimDate = new DateTime(claimDateAnswer.dateOfClaim.year.get, claimDateAnswer.dateOfClaim.month.get, claimDateAnswer.dateOfClaim.day.get, 0, 0)
    claimDate.isAfter(monthsFuture)
  }

  private def decisionElement(assistedDecision: AssistedDecisionDetails) = <AssistedDecisions><AssistedDecision><Reason>{assistedDecision.reason}</Reason><RecommendedDecision>{assistedDecision.recommendation}</RecommendedDecision></AssistedDecision></AssistedDecisions>

  private def decisionModel(reason: String, decision: String): AssistedDecisionDetails = new AssistedDecisionDetails(reason, decision)

  private def process(decision: AssistedDecisionDetails => Boolean, claim: Claim)(fns: Array[(Claim) => AssistedDecisionDetails]): AssistedDecisionDetails = {
    for (f <- fns) {
      val result = f(claim)
      if (decision(result)) return result
    }
    noDecisionSoFar
  }

  def fromXml(xml: NodeSeq, claim: Claim): Claim = {
    val decisions = (xml \\ "AssistedDecisions" \ "AssistedDecision")
    val assistedDecisionDetails = AssistedDecisionDetails(reason = (decisions \ "Reason").text, recommendation = (decisions \ "RecommendedDecision").text)
    claim.update(assistedDecisionDetails)
  }
}
