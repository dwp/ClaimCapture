package xml

import models.domain._
import XMLHelper.formatValue
import scala.xml.NodeSeq
import app.{PensionPaymentFrequency, StatutoryPaymentFrequency}
import app.XMLValues._
import org.joda.time.format.DateTimeFormat
import org.joda.time.DateTime
import play.api.i18n.Messages

object EvidenceList {

  def xml(claim: Claim) = {
    <EvidenceList>
      {xmlGenerated()}{evidence(claim)}{aboutYou(claim)}{yourPartner(claim)}{careYouProvide(claim)}{breaks(claim)}{employment(claim)}{selfEmployment(claim)}{otherMoney(claim)}{AssistedDecision.xml(claim)}
    </EvidenceList>
  }

  def xmlGenerated() = {
    textLine("XML Generated at: " + DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss").print(DateTime.now()))
  }


  def evidence(claim: Claim): NodeSeq = {
    val employment = claim.questionGroup[models.domain.Employment].getOrElse(models.domain.Employment())
    val employed = employment.beenEmployedSince6MonthsBeforeClaim == yes
    val selfEmployed = employment.beenSelfEmployedSince1WeekBeforeClaim == yes
    val claimDate = claim.questionGroup[ClaimDate].getOrElse(ClaimDate())

    val buffer = NodeSeq.Empty

    if (employed || selfEmployed) {
      buffer ++ textLine("Send us the following documents below including your Name and National Insurance (NI) number.")

      if (employed) {
        buffer ++ textLine()
        buffer ++ textLine("Your Employment documents.")
        buffer ++ textLine("Last payslip you got before your claim date: ", claimDate.dateOfClaim.`dd/MM/yyyy`)
        buffer ++ textLine("Any payslips you have had since then.")
        buffer ++ textLine("Any pension statements you may have.")

      }

      if (selfEmployed) {
        buffer ++ textLine()
        buffer ++ textLine("Your Self-employed documents.")
        buffer ++ textLine("Most recent finalised accounts you have for your business.")
        buffer ++ textLine("Any pension statements you may have.")
      }

      buffer ++ textLine()
      buffer ++ textLine("Send the above documents to:")
      buffer ++ textLine("CA Freepost")
      buffer ++ textLine("Palatine House")
      buffer ++ textLine("Preston")
      buffer ++ textLine("PR1 1HN")
      buffer ++ textLine("The Carer's Allowance unit will contact you if they need any further information.")
      buffer ++ textLine()
    }

    buffer
  }

  def aboutYou(claim: Claim) = {
    val nationalityAndResidency = claim.questionGroup[NationalityAndResidency].getOrElse(NationalityAndResidency())
    val yourContactDetails = claim.questionGroup[ContactDetails].getOrElse(ContactDetails())
    val moreAboutYou = claim.questionGroup[MoreAboutYou].getOrElse(MoreAboutYou())
    val otherEEAState = claim.questionGroup[OtherEEAStateOrSwitzerland].getOrElse(OtherEEAStateOrSwitzerland())

    var textLines = NodeSeq.Empty ++ textSeparatorLine("About You")
    textLines ++= textLine(Messages("resideInUK.label") + " = ", nationalityAndResidency.resideInUK.answer)
    textLines ++= fiftyTwoWeeksTrips(claim)
    textLines ++= textLine("Mobile number = ", yourContactDetails.mobileNumber)
    textLines ++= textLine("Do you, or any member of your family, receive any benefits or pensions from from a European Economic Area (EEA) state or Switzerland? = ", otherEEAState.benefitsFromOtherEEAStateOrSwitzerland)
    textLines ++= textLine("Have you, or a member of your family, made a claim for any benefits or pensions from a European Economic Area (EEA) state or Switzerland? = ", otherEEAState.claimedForBenefitsFromOtherEEAStateOrSwitzerland)
    textLines ++= textLine("Are you, or a member of your family, working in or paying insurance to, another European Economic Area (EEA) state or Switzerland? = ", otherEEAState.workingForOtherEEAStateOrSwitzerland)
    textLines ++= textLine("Do you get state Pension? = ", moreAboutYou.receiveStatePension) ++
      textLine("If you have speech or hearing difficulties, would you like us to contact you by textphone? = ", yourContactDetails.contactYouByTextphone)

    textLines
  }

  def yourPartner(claim: Claim) = {
    val personYouCareFor = claim.questionGroup[YourPartnerPersonalDetails].getOrElse(YourPartnerPersonalDetails())

    if (personYouCareFor.isPartnerPersonYouCareFor.nonEmpty) {
      textSeparatorLine("About Your Partner") ++
        textLine("Is your partner/spouse the person you are claiming Carer's Allowance for? = ", personYouCareFor.isPartnerPersonYouCareFor)
    }
  }

  def careYouProvide(claim: Claim) = {
    val theirPersonalDetails = claim.questionGroup[TheirPersonalDetails].getOrElse(TheirPersonalDetails())
    val moreAboutThePerson = claim.questionGroup[MoreAboutThePerson].getOrElse(MoreAboutThePerson())
    val moreAboutTheCare = claim.questionGroup[MoreAboutTheCare].getOrElse(MoreAboutTheCare())

    textSeparatorLine("About Care You Provide") ++
      textLine("Do they live at the same address as you? = ", theirPersonalDetails.liveAtSameAddressCareYouProvide) ++
      textLine("Does this person get Armed Forces Independence Payment? = ", moreAboutThePerson.armedForcesPayment) ++
      textLine("Do you spend 35 hours or more each week caring for this person? = ", moreAboutTheCare.spent35HoursCaring) ++
      textLine("Did you care for this person for 35 hours or more each week before your claim date ? = ", moreAboutTheCare.spent35HoursCaringBeforeClaim.answer)
  }

  def breaks(claim: Claim) = {
    val breaksInCare = claim.questionGroup[BreaksInCare].getOrElse(BreaksInCare())

    for {break <- breaksInCare.breaks} yield {
      textLine("Where were you during the break? Other detail = ", break.whereYou.other) ++
        textLine("Where was the person you care for during the break? = ", break.wherePerson.location) ++
        textLine("Where was the person you care for during the break? Other detail = ", break.wherePerson.other)
    }
  }

  def fiftyTwoWeeksTrips(claim: Claim):NodeSeq = {
    import scala.language.postfixOps

    val trips = claim.questionGroup[Trips].getOrElse(Trips())
    val claimDate = claim.questionGroup[ClaimDate].getOrElse(ClaimDate())
    var textLines = NodeSeq.Empty

    textLines ++= textLine("Have you been out of England, Scotland or Wales for more than 52 weeks in the last 3 years before your claim date " +
      s" ${(claimDate.dateOfClaim - 3 years).`dd/MM/yyyy`}? = ", if (trips.fiftyTwoWeeksTrips.size > 0) Yes else No)

    for ((fiftyTwoWeekTrip,index) <- trips.fiftyTwoWeeksTrips.zipWithIndex) {
      if (index > 0){
        textLines ++= textLine("Have you been out of England, Scotland or Wales at any other time in the last 3 years before your claim date" +
          s" ${(claimDate.dateOfClaim - 3 years).`dd/MM/yyyy`}? = ", if (trips.fiftyTwoWeeksTrips.size > 0) Yes else No)
      }
      textLines ++= textLine("Which country did you go to? = ", fiftyTwoWeekTrip.where)

      fiftyTwoWeekTrip.start match {
        case Some(dayMonthYear) => textLines ++= textLine("Date you left = ", dayMonthYear.`yyyy-MM-dd`)
        case _ => NodeSeq.Empty
      }

      fiftyTwoWeekTrip.end match {
        case Some(dayMonthYear) => textLines ++= textLine("Date you returned = ", dayMonthYear.`yyyy-MM-dd`)
        case _ => NodeSeq.Empty
      }

      fiftyTwoWeekTrip.why match {
        case Some(reasonForBeingThere) => reasonForBeingThere.reason match {
          case Some (reason) =>
            var reasonText = "Reason for being there? = " + reason
            if (reasonForBeingThere.other.isDefined) reasonText = "Reason for being there? Other =  " +reasonForBeingThere.other.get
            textLines ++= textLine(reasonText)
          case _ => NodeSeq.Empty
        }
        case _ => NodeSeq.Empty
      }

      textLines ++= textLine("Was the person you care for with you? = ", fiftyTwoWeekTrip.personWithYou)
    }
    textLines
  }

  def selfEmployment(claim: Claim) = {
    val yourAccounts = claim.questionGroup[SelfEmploymentYourAccounts].getOrElse(SelfEmploymentYourAccounts())
    val childCare = claim.questionGroup[ChildcareExpensesWhileAtWork].getOrElse(ChildcareExpensesWhileAtWork())
    val expensesWhileAtWork = claim.questionGroup[ExpensesWhileAtWork].getOrElse(ExpensesWhileAtWork())
    val pensionScheme = claim.questionGroup[SelfEmploymentPensionsAndExpenses].getOrElse(SelfEmploymentPensionsAndExpenses())

    var textLines = textLine("Are the income, outgoings and profit in these accounts similar to your current level of trading? = ", yourAccounts.areIncomeOutgoingsProfitSimilarToTrading) ++
      textLine("Please tell us why and when the change happened = ", yourAccounts.tellUsWhyAndWhenTheChangeHappened) ++
      textLine("How much [[past=did you]] [[present=do you]] pay them - expenses related to childcare? = ", childCare.howMuchYouPay) ++
      textLine("How often [[past=did you]] [[present=do you]] - expenses related to childcare expenses? = ", PensionPaymentFrequency.mapToHumanReadableString(childCare.howOftenPayChildCare))
    if (childCare.howOftenPayChildCare.other.isDefined)
      textLines ++= textLine("How often [[past=did you]] [[present=do you]] Other - expenses related to childcare expenses? = ", childCare.howOftenPayChildCare.other.get)
    if (childCare.relationToPartner.nonEmpty)
      textLines ++= textLine(Messages("relationToPartner") + " = ", childCare.relationToPartner.get)

    textLines ++= textLine("How much [[past=did you]] [[present=do you]] pay them - expenses related to person you care for? = ", expensesWhileAtWork.howMuchYouPay) ++
      textLine("How often [[past=did you]] [[present=do you]] - expenses related to person you care for? = ", PensionPaymentFrequency.mapToHumanReadableString(expensesWhileAtWork.howOftenPayExpenses))

    if (expensesWhileAtWork.howOftenPayExpenses.other.isDefined)
      textLines ++= textLine("How often [[past=did you]] [[present=do you]] Other - expenses related to person you care for? = ", expensesWhileAtWork.howOftenPayExpenses.other.get)

    if (pensionScheme.howOften.isDefined && pensionScheme.howOften.get.other.isDefined)
      textLines ++= textLine("How often do you pay into a Pension? Other = ", pensionScheme.howOften.get.other.get)

    if (sectionEmpty(textLines)) NodeSeq.Empty else textSeparatorLine("Self Employment") ++ textLines
  }

  def employment(claim: Claim) = {
    claim.questionGroup[Jobs] match {
      case Some(jobs) =>
        var textLines = NodeSeq.Empty
        textLines ++= textSeparatorLine("Employment")

        for (job <- jobs) {

          val jobDetails = job.questionGroup[JobDetails].getOrElse(JobDetails())
          val lastWage = job.questionGroup[LastWage].getOrElse(LastWage())
          val childcareExpenses = job.questionGroup[ChildcareExpenses].getOrElse(ChildcareExpenses())
          val personYouCareForExpenses = job.questionGroup[PersonYouCareForExpenses].getOrElse(PersonYouCareForExpenses())
          val pensionScheme = job.questionGroup[PensionSchemes].getOrElse(PensionSchemes())

          textLines ++= textLine("Employer:" + jobDetails.employerName)
          if (jobDetails.p45LeavingDate.isDefined)
            textLines ++= textLine("What is the leaving date on your P45, if you have one? = ", jobDetails.p45LeavingDate.get.`dd/MM/yyyy`)

          if (lastWage.sameAmountEachTime.isDefined)
            textLines ++= textLine("About your wage,[[past=Did you]] [[present=Do you]] get the same amount each time? = ", lastWage.sameAmountEachTime.get)

          if (childcareExpenses.howMuchCostChildcare.nonEmpty)
            textLines ++= textLine("How much [[past=did you]] [[present=do you]] pay them - expenses related to childcare expenses? = ", childcareExpenses.howMuchCostChildcare)
          textLines ++= textLine("How often [[past=did you]] [[present=do you]] - expenses related to childcare expenses? = ", PensionPaymentFrequency.mapToHumanReadableString(childcareExpenses.howOftenPayChildCare))
          if (childcareExpenses.howOftenPayChildCare.other.isDefined)
            textLines ++= textLine("How often [[past=did you]] [[present=do you]] Other - expenses related to childcare expenses? = ", childcareExpenses.howOftenPayChildCare.other.get)
          if (childcareExpenses.relationToPartner.nonEmpty)
            textLines ++= textLine(Messages("relationToPartner") + " = ", childcareExpenses.relationToPartner.get)

          if (personYouCareForExpenses.howMuchCostCare.nonEmpty)
            textLines ++= textLine("How much [[past=did you]] [[present=do you]] pay them - expenses related to person you care for? = ", personYouCareForExpenses.howMuchCostCare)
          textLines ++= textLine("How often [[past=did you]] [[present=do you]] - expenses related to the person you care for? = ", PensionPaymentFrequency.mapToHumanReadableString(personYouCareForExpenses.howOftenPayCare))
          if (personYouCareForExpenses.howOftenPayCare.other.isDefined)
            textLines ++= textLine("How often [[past=did you]] [[present=do you]] Other - expenses related to the person you care for? = ", personYouCareForExpenses.howOftenPayCare.other.get)

          if (pensionScheme.howOftenPension.isDefined && pensionScheme.howOftenPension.get.frequency == PensionPaymentFrequency.Other)
            textLines ++= textLine("How often other - Occupational Pension Scheme? = ", pensionScheme.howOftenPension.get.other.getOrElse(""))
          if (pensionScheme.howOftenPersonal.isDefined && pensionScheme.howOftenPersonal.get.frequency == PensionPaymentFrequency.Other)
            textLines ++= textLine("How often other - Personal Pension Scheme? = ", pensionScheme.howOftenPersonal.get.other.getOrElse(""))
          textLine()
        }

        textLines
      case None => NodeSeq.Empty
    }
  }

  def otherMoney(claim: Claim) = {
    val aboutOtherMoney = claim.questionGroup[AboutOtherMoney].getOrElse(AboutOtherMoney())
    val statutorySickPay = claim.questionGroup[StatutorySickPay].getOrElse(StatutorySickPay())
    val otherStatutoryPay = claim.questionGroup[OtherStatutoryPay].getOrElse(OtherStatutoryPay())

    val aboutOtherMoney_howOftenOther = aboutOtherMoney.howOften match {
      case Some(s) => s.other.getOrElse("")
      case _ => ""
    }
    val ssp_howOftenOther = statutorySickPay.howOften match {
      case Some(s) => s.other.getOrElse("")
      case _ => ""
    }
    val smp_howOftenOther = otherStatutoryPay.howOften match {
      case Some(s) => s.other.getOrElse("")
      case _ => ""
    }

    textSeparatorLine("Other Money") ++
      textLine("Have you <or your partner/spouse> claimed or received any other benefits since the date you want to claim? = ", aboutOtherMoney.yourBenefits.answer) ++
      textLine("Have you received any payments for the person you care for or any other person since your claim date? = ", aboutOtherMoney.anyPaymentsSinceClaimDate.answer) ++
      textLine("Details about other money: Who pays you? = ", aboutOtherMoney.whoPaysYou) ++
      textLine("Details about other money: How much? = ", aboutOtherMoney.howMuch) ++
      textLine("Details about other money: How often? = ", StatutoryPaymentFrequency.mapToHumanReadableStringWithOther(aboutOtherMoney.howOften)) ++
      textLine("Details about other money: How often other? = ", aboutOtherMoney_howOftenOther) ++
      textLine("Statutory Sick Pay: How much? = ", statutorySickPay.howMuch) ++
      textLine("Statutory Sick Pay: How often? = ", StatutoryPaymentFrequency.mapToHumanReadableStringWithOther(statutorySickPay.howOften)) ++
      textLine("Statutory Sick Pay: How often other? = ", ssp_howOftenOther) ++
      textLine("Other Statutory Pay: How much? = ", otherStatutoryPay.howMuch) ++
      textLine("Other Statutory Pay: How often? = ", StatutoryPaymentFrequency.mapToHumanReadableStringWithOther(otherStatutoryPay.howOften)) ++
      textLine("Other Statutory Pay: How often other? = ", smp_howOftenOther)
  }

  private def textSeparatorLine(title: String) = {
    val lineWidth = 54
    val padding = "=" * ((lineWidth - title.length) / 2)

    <TextLine>
      {s"$padding$title$padding"}
    </TextLine>
  }


  private def sectionEmpty(nodeSeq: NodeSeq) = {
    if (nodeSeq == null || nodeSeq.isEmpty) true else nodeSeq.text.isEmpty
  }

  private def textLine(): NodeSeq = <TextLine/>

  private def textLine(text: String): NodeSeq = <TextLine>
    {text}
  </TextLine>

  private def textLine(label: String, value: String): NodeSeq = value match {
    case "" => NodeSeq.Empty
    case _ => <TextLine>
      {s"$label" + formatValue(value)}
    </TextLine>
  }

  private def textLine(label: String, value: Option[String]): NodeSeq = value match {
    case Some(s) => textLine(label, value.get)
    case None => NodeSeq.Empty
  }
}