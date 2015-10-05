package xml.claim

import app.StatutoryPaymentFrequency
import models.domain._
import models.PaymentFrequency
import scala.xml.{Node, NodeSeq}
import org.joda.time.DateTime
import scala.Some
import xml.XMLComponent
import xml.XMLHelper._
import models.DayMonthYear


/**
 * Generate the XML presenting the Assisted decisions.
 * @author Jorge Migueis
 */
object AssistedDecision extends XMLComponent {

  def xml(claim: Claim) = {

    // Business postponed some assisted decisions; Thus the commented code.
    //    var assisted = caringHours(claim)
    //    if (assisted.length == 0 ) {
    //      assisted ++= employmentGrossPay(claim)
    var assisted = NodeSeq.Empty
    assisted ++= noEEABenefits(claim)
    assisted ++= noEEAWork(claim)
    assisted ++= nationalityCheck(claim)
    assisted ++= normallyResideInUK(claim)
    //    }
    //    assisted ++= dateOfClaim(claim)
    //    assisted ++= rightAge(claim)
    if (assisted.length > 0) <AssistedDecisions>{assisted}</AssistedDecisions>
    else NodeSeq.Empty
  }

  // ============ Decision functions ====================

  private def caringHours(claim: Claim): NodeSeq = {
    val hours = claim.questionGroup[MoreAboutTheCare].getOrElse(MoreAboutTheCare())
    if (hours.spent35HoursCaring.toLowerCase != "yes") decisionElement("Do not spend 35 hours or more each week caring.","Potential disallowance, but need to check advisory additional notes.")
    else NodeSeq.Empty
  }

  private def employmentGrossPay(claim: Claim): NodeSeq = {
    // Weekly earning requirements
    //    Have you been employed at any time since <ddmmyyyy_1> (this is six months before your claim date:< ddmmyyyy>)? = Yes
    //    AND What was the  gross pay for this period? is > £100 for a week, £200.01 for 2 weeks, £400.03 for 4 weeks, £433.37 for a month
    //    AND No is answered to all Pensions and Expenses
    //    AND get same amount each time for a job
    var weeklyEarning: Double = 0.0d
    claim.questionGroup[Jobs] match {
      case Some(jobs) => for (job <- jobs) {
        val lastWage = job.questionGroup[LastWage].getOrElse(LastWage("", PaymentFrequency(), "",DayMonthYear(),"", None, "", None))
        if (weeklyEarning > -1d && lastWage.sameAmountEachTime.toLowerCase == "yes") {

          if (!job.questionGroup[PensionAndExpenses].isDefined
            && (!job.questionGroup[PensionAndExpenses].isDefined || (job.questionGroup[PensionAndExpenses].get.payPensionScheme.answer.toLowerCase != "yes" && job.questionGroup[PensionAndExpenses].get.haveExpensesForJob.answer.toLowerCase != "yes"))) {
            val earning = currencyAmount(lastWage.grossPay).toDouble
            //            Logger.debug("Assisted decision - Pay frequency " + job.questionGroup[AdditionalWageDetails].getOrElse(AdditionalWageDetails()).oftenGetPaid.frequency)
            val frequencyFactor: Double = lastWage.oftenGetPaid.frequency match {
              case StatutoryPaymentFrequency.Weekly => 1.0
              case StatutoryPaymentFrequency.Fortnightly => 2.0001
              case StatutoryPaymentFrequency.FourWeekly => 4.0003
              case StatutoryPaymentFrequency.Monthly => 4.3337
              case _ => 0d
            }
            if (frequencyFactor == 0) {
              if (weeklyEarning <= 100.00) weeklyEarning = -1 // We do no know frequency so we cannot compute earning and assist the decision. If we had already > 100 then do not change decision.
            }
            else weeklyEarning += earning / frequencyFactor
          }
          else weeklyEarning = -1 // A pension or expense is linked to a job so we cannot trigger nil decision
        }
      }
      case None => 0.0f
    }
    if (weeklyEarning > 100.0d) decisionElement(s"Total weekly gross pay ${"%.2f".format((weeklyEarning * 100).ceil / 100d)} > £100.","Potential disallowance, but need to check advisory additional notes.")
    else NodeSeq.Empty
  }

  private def rightAge(claim: Claim): NodeSeq = {
    val yourDetails = claim.questionGroup[YourDetails].getOrElse(YourDetails())
    val sixteenYearsAgo = DateTime.now().minusYears(16)
    if (yourDetails.dateOfBirth.year.isDefined) {
      val dob = new DateTime(yourDetails.dateOfBirth.year.get, yourDetails.dateOfBirth.month.get, yourDetails.dateOfBirth.day.get, 0, 0)
      if (dob.isAfter(sixteenYearsAgo)) decisionElement(s"Customer Date of Birth ${yourDetails.dateOfBirth.`dd/MM/yyyy`} is < 16 years old.","Potential disallowance, but need to check advisory additional notes.")
      else NodeSeq.Empty
    } else NodeSeq.Empty
  }

  private def dateOfClaim(claim: Claim): NodeSeq = {
    val claimDateAnswer = claim.questionGroup[ClaimDate].getOrElse(ClaimDate())
    val monthsFuture = DateTime.now().plusMonths(3).plusDays(1)
    val claimDate = new DateTime(claimDateAnswer.dateOfClaim.year.get, claimDateAnswer.dateOfClaim.month.get, claimDateAnswer.dateOfClaim.day.get, 0, 0)
    if (claimDate.isAfter(monthsFuture)) decisionElement("Date of Claim too far in the future.", "Potential disallowance.")
    else NodeSeq.Empty
  }

  private def nationalityCheck(claim: Claim): NodeSeq = {
    val nationalityAndResidency = claim.questionGroup[NationalityAndResidency].getOrElse(NationalityAndResidency(""))
    if (nationalityAndResidency.nationality == NationalityAndResidency.anothercountry) decisionElement("Person is not British.", "Transfer to Exportability team.")
    else NodeSeq.Empty
  }

  private def normallyResideInUK(claim: Claim): NodeSeq = {
    val nationalityAndResidency = claim.questionGroup[NationalityAndResidency].getOrElse(NationalityAndResidency(""))
    if (nationalityAndResidency.resideInUK.answer.toLowerCase != "yes") decisionElement("Person does not normally live in England, Scotland or Wales.", "Transfer to Exportability team.")
    else NodeSeq.Empty
  }

  private def noEEABenefits(claim: Claim): NodeSeq = {
    val otherEEAStateOrSwitzerland = claim.questionGroup[OtherEEAStateOrSwitzerland].getOrElse(OtherEEAStateOrSwitzerland())
    if (otherEEAStateOrSwitzerland.guardQuestion.field1.fold(false)(_.answer.toLowerCase == "yes")) decisionElement("Claimant or partner dependent on EEA pensions or benefits.", "Transfer to Exportability team.")
    else NodeSeq.Empty
  }

  private def noEEAWork(claim: Claim): NodeSeq = {
    val otherEEAStateOrSwitzerland = claim.questionGroup[OtherEEAStateOrSwitzerland].getOrElse(OtherEEAStateOrSwitzerland())
    if (otherEEAStateOrSwitzerland.guardQuestion.field2.fold(false)(_.answer.toLowerCase == "yes"))decisionElement("Claimant or partner dependent on EEA insurance or work.", "Transfer to Exportability team.")
    else NodeSeq.Empty
  }

  private def decisionElement(reason: String, decision:String) = <AssistedDecision><Reason>{reason}</Reason><RecommendedDecision>{decision}</RecommendedDecision></AssistedDecision>

}
