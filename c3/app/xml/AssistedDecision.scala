package xml

import app.StatutoryPaymentFrequency
import models.domain._
import scala.xml.NodeSeq
import org.joda.time.DateTime
import models.domain.Claim
import scala.Some
import play.api.Logger

/**
 * Generate the XML presenting the Assisted decisions.
 * @author Jorge Migueis
 */
object AssistedDecision {

  def xml(claim: Claim) = {
    var assisted = caringHours(claim)
    if (assisted.length == 0 ) {
      assisted ++= employmentGrossPay(claim)
      assisted ++= getAFIP(claim)
      assisted ++= noEEABenefits(claim)
      assisted ++= noEEAWork(claim)
      assisted ++= inGBNow(claim)
    }
    assisted ++= dateOfClaim(claim)
    assisted ++= rightAge(claim)
    if (assisted.length > 0) textSeparatorLine("Assisted Decision") ++ assisted
    else NodeSeq.Empty
  }

  // ============ Decision functions ====================

  private def caringHours(claim: Claim): NodeSeq = {
    val hours = claim.questionGroup[MoreAboutTheCare].getOrElse(MoreAboutTheCare())
    if (hours.spent35HoursCaring.toLowerCase != "yes") textLine("Do not spend 35 hours or more each week caring. Potential disallowance, but need to check advisory additional notes.")
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
        val lastWage = job.questionGroup[LastWage].getOrElse(LastWage())
        if (weeklyEarning > -1d && lastWage.sameAmountEachTime.getOrElse("").toLowerCase == "yes") {
//          Logger.info(">>>>> child expense " + job.questionGroup[ChildcareExpenses])
//          Logger.info(">>>>> Person you care expenses " + job.questionGroup[PersonYouCareForExpenses])
//          Logger.info(">>>>> Pension schemes " + job.questionGroup[PensionSchemes])
          if (!job.questionGroup[ChildcareExpenses].isDefined && !job.questionGroup[PersonYouCareForExpenses].isDefined
            && (!job.questionGroup[PensionSchemes].isDefined || (job.questionGroup[PensionSchemes].get.payPersonalPensionScheme.toLowerCase != "yes" && job.questionGroup[PensionSchemes].get.payOccupationalPensionScheme.toLowerCase != "yes"))) {
            val earning = lastWage.grossPay.toDouble
            Logger.info(">>>>>> Pay frequency " + job.questionGroup[AdditionalWageDetails].getOrElse(AdditionalWageDetails()).oftenGetPaid.frequency)
            val frequencyFactor: Double = job.questionGroup[AdditionalWageDetails].getOrElse(AdditionalWageDetails()).oftenGetPaid.frequency match {
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
    if (weeklyEarning > 100.0d) textLine(s"Total weekly gross pay ${"%.2f".format((weeklyEarning * 100).ceil / 100d)} > £100. Potential disallowance, but need to check advisory additional notes.")
    else NodeSeq.Empty
  }

  private def rightAge(claim: Claim): NodeSeq = {
    val yourDetails = claim.questionGroup[YourDetails].getOrElse(YourDetails())
    val sixteenYearsAgo = DateTime.now().minusYears(16)
    if (yourDetails.dateOfBirth.year.isDefined) {
      val dob = new DateTime(yourDetails.dateOfBirth.year.get, yourDetails.dateOfBirth.month.get, yourDetails.dateOfBirth.day.get, 0, 0)
      if (dob.isAfter(sixteenYearsAgo)) textLine(s"Customer Date of Birth ${yourDetails.dateOfBirth.`dd/MM/yyyy`} is < 16 years old. Potential disallowance, but need to check advisory additional notes.")
      else NodeSeq.Empty
    } else NodeSeq.Empty
  }

  private def getAFIP(claim: Claim): NodeSeq = {
    val moreAboutThePerson = claim.questionGroup[MoreAboutThePerson].getOrElse(MoreAboutThePerson())
    if (moreAboutThePerson.armedForcesPayment.toLowerCase == "yes") textLine("Person receives Armed Forces Independence Payment. Transfer to Armed Forces Independent Payments team.")
    else NodeSeq.Empty
  }

  private def noEEABenefits(claim: Claim): NodeSeq = {
    val otherEEAStateOrSwitzerland = claim.questionGroup[OtherEEAStateOrSwitzerland].getOrElse(OtherEEAStateOrSwitzerland())
    if (otherEEAStateOrSwitzerland.benefitsFromOtherEEAStateOrSwitzerland.toLowerCase == "yes") textLine("Claimant or partner dependent on EEA pensions or benefits. Transfer to Exportability team or potential disallowance.")
    else NodeSeq.Empty
  }

  private def noEEAWork(claim: Claim): NodeSeq = {
    val otherEEAStateOrSwitzerland = claim.questionGroup[OtherEEAStateOrSwitzerland].getOrElse(OtherEEAStateOrSwitzerland())
    if (otherEEAStateOrSwitzerland.workingForOtherEEAStateOrSwitzerland.toLowerCase == "yes") textLine("Claimant or partner dependent on EEA insurance or work. Transfer to Exportability team or potential disallowance.")
    else NodeSeq.Empty
  }

  private def dateOfClaim(claim:Claim) : NodeSeq = {
    val claimDateAnswer = claim.questionGroup[ClaimDate].getOrElse(ClaimDate())
    val monthsFuture = DateTime.now().plusMonths(3).plusDays(1)
    val claimDate = new DateTime(claimDateAnswer.dateOfClaim.year.get, claimDateAnswer.dateOfClaim.month.get,claimDateAnswer.dateOfClaim.day.get,0,0)
    if (claimDate.isAfter(monthsFuture)) textLine("Date of Claim too far in the future. Potential disallowance.")
    else NodeSeq.Empty
  }

  private def inGBNow(claim:Claim) : NodeSeq = {
    val isInGBNow = claim.questionGroup[NormalResidenceAndCurrentLocation].getOrElse(NormalResidenceAndCurrentLocation())
    if (isInGBNow.inGBNow.toLowerCase != "yes") textLine("Person does not reside in GB now. Transfer to Exportability team or potential disallowance.")
    else NodeSeq.Empty
  }

  // =========== Formatting Functions ===================

  private def textSeparatorLine(title: String): NodeSeq = {
    val lineWidth = 54
    val padding = "=" * ((lineWidth - title.length) / 2)

    <TextLine>
      {s"$padding$title$padding"}
    </TextLine>

  }

  private def textLine(text: String) = <TextLine>{text}</TextLine>

}
