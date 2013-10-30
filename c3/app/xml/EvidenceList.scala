package xml

import models.domain._
import app.{PensionPaymentFrequency, StatutoryPaymentFrequency}
import app.XMLValues._
import models.domain.Claim
import xml.XMLHelper._
import scala.xml.{NodeBuffer, Elem, NodeSeq}
import org.joda.time.format.DateTimeFormat
import org.joda.time.DateTime

object EvidenceList {

  def buildXml(claim: Claim) = {
    val theirContactDetails = claim.questionGroup[TheirContactDetails].getOrElse(TheirContactDetails())

    <EvidenceList>
      {xml.XMLHelper.postalAddressStructureRecipientAddress(theirContactDetails.address, theirContactDetails.postcode.orNull)}
      {xmlGenerated()}
      {evidence(claim)}
      {aboutYou(claim)}
      {yourPartner(claim)}
      {breaks(claim)}
      {fiftyTwoWeeksTrips(claim)}
      {employment(claim)}
      {selfEmployment(claim)}
      {otherMoney(claim)}
    </EvidenceList>
  }

  def xmlGenerated() = {
    textLine("XML Generated at: "+DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss").print(DateTime.now()))
  }


  def evidence(claim: Claim): NodeBuffer = {
    val employment = claim.questionGroup[models.domain.Employment].getOrElse(models.domain.Employment())
    val employed = employment.beenEmployedSince6MonthsBeforeClaim == yes
    val selfEmployed = employment.beenSelfEmployedSince1WeekBeforeClaim == yes
    val claimDate = claim.questionGroup[ClaimDate].getOrElse(ClaimDate())

    val buffer = new NodeBuffer

    if (employed || selfEmployed) {
      buffer += textLine("Send us the following documents below including your Name and National Insurance (NI) number.")

      if (employed) {
        buffer += textLine("Your Employment documents.")
        buffer += textLine("Last payslip you got before your claim date: ", claimDate.dateOfClaim.`dd/MM/yyyy`)
        buffer += textLine("Any payslips you have had since then.")
        buffer += textLine("Any pension statements you may have.")

      }

      if (selfEmployed) {
        buffer += textLine("Your Self-employed documents.")
        buffer += textLine("Most recent finalised accounts you have for your business.")
        buffer += textLine("Any pension statements you may have.")
      }

      buffer += textLine("Send the above documents to:")
      buffer += textLine("CA Freepost")
      buffer += textLine("Palatine House")
      buffer += textLine("Preston")
      buffer += textLine("PR1 1HN")
      buffer += textLine("The Carer's Allowance unit will contact you if they need any further information.")
    }

    buffer
  }

  def aboutYou(claim: Claim) = {
    val yourDetails = claim.questionGroup[YourDetails].getOrElse(YourDetails())
    val yourContactDetails = claim.questionGroup[ContactDetails].getOrElse(ContactDetails())
    val timeOutsideUK = claim.questionGroup[TimeOutsideUK].getOrElse(TimeOutsideUK())
    val moreAboutYou = claim.questionGroup[MoreAboutYou].getOrElse(MoreAboutYou())
    var textLines = NodeSeq.Empty
    textLines ++= textLine("Have you always lived in the UK? = ", yourDetails.alwaysLivedUK) ++
      textLine("Mobile number = ", yourContactDetails.mobileNumber) ++
      textLine("Are you currently living in the UK? = ", timeOutsideUK.livingInUK.answer)
    if (timeOutsideUK.livingInUK.answer.toLowerCase == yes)
      textLines ++= textLine("When did you arrive in the UK? = ", timeOutsideUK.livingInUK.date.get.`dd/MM/yyyy`)
    textLines ++= textLine("Do you get state Pension? = ", moreAboutYou.receiveStatePension) ++
      textLine("If you have speech or hearing difficulties, would you like us to contact you by textphone? = ", yourContactDetails.contactYouByTextphone)

    textLines
  }

  def yourPartner(claim: Claim) = {
    val personYouCareFor = claim.questionGroup[PersonYouCareFor].getOrElse(PersonYouCareFor())

    if (personYouCareFor.isPartnerPersonYouCareFor.nonEmpty) {
        textLine("Is your partner/spouse the person you are claiming Carer's Allowance for? = ", personYouCareFor.isPartnerPersonYouCareFor)
    }
  }

  def breaks(claim: Claim) = {
    val breaksInCare = claim.questionGroup[BreaksInCare].getOrElse(BreaksInCare())

    for {break <- breaksInCare.breaks} yield {
      textLine("Where were you during the break? Other detail = ", break.whereYou.other) ++
        textLine("Where was the person you care for during the break? = ", break.wherePerson.location) ++
        textLine("Where was the person you care for during the break? Other detail = ", break.wherePerson.other)
    }
  }

  def fiftyTwoWeeksTrips(claim: Claim) = {
    val trips = claim.questionGroup[Trips].getOrElse(Trips())
    for {fiftyTwoWeekTrip <- trips.fiftyTwoWeeksTrips} yield textLine("Where did you go? = ", fiftyTwoWeekTrip.where)
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

    textLines ++= textLine("How much [[past=did you]] [[present=do you]] pay them - expenses related to person you care for? = ", expensesWhileAtWork.howMuchYouPay) ++
      textLine("How often [[past=did you]] [[present=do you]] - expenses related to person you care for? = ", PensionPaymentFrequency.mapToHumanReadableString(expensesWhileAtWork.howOftenPayExpenses))

    if (expensesWhileAtWork.howOftenPayExpenses.other.isDefined)
      textLines ++= textLine("How often [[past=did you]] [[present=do you]] Other - expenses related to person you care for? = ", expensesWhileAtWork.howOftenPayExpenses.other.get)

    if (pensionScheme.howOften.isDefined && pensionScheme.howOften.get.other.isDefined)
      textLines ++= textLine("How often do you pay into a Pension? Other = ", pensionScheme.howOften.get.other.get)

    if (sectionEmpty(textLines)) NodeSeq.Empty else textLines
  }

  def employment(claim: Claim) = {
    claim.questionGroup[Jobs] match {
      case Some(jobs) =>
        var textLines = NodeSeq.Empty

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

          if (personYouCareForExpenses.howMuchCostCare.nonEmpty)
            textLines ++= textLine("How much [[past=did you]] [[present=do you]] pay them - expenses related to person you care for? = ", personYouCareForExpenses.howMuchCostCare)
          textLines ++= textLine("How often [[past=did you]] [[present=do you]] - expenses related to the person you care for? = ", PensionPaymentFrequency.mapToHumanReadableString(personYouCareForExpenses.howOftenPayCare))
          if (personYouCareForExpenses.howOftenPayCare.other.isDefined)
            textLines ++= textLine("How often [[past=did you]] [[present=do you]] Other - expenses related to the person you care for? = ", personYouCareForExpenses.howOftenPayCare.other.get)

          if (pensionScheme.howOftenPension.isDefined && pensionScheme.howOftenPension.get.frequency == PensionPaymentFrequency.Other)
            textLines ++= textLine("How often other - Occupational Pension Scheme? = ", pensionScheme.howOftenPension.get.other.getOrElse(""))
          if (pensionScheme.howOftenPersonal.isDefined && pensionScheme.howOftenPersonal.get.frequency == PensionPaymentFrequency.Other)
            textLines ++= textLine("How often other - Personal Pension Scheme? = ", pensionScheme.howOftenPersonal.get.other.getOrElse(""))
        }

        textLines
      case None => NodeSeq.Empty
    }
  }

  def otherMoney(claim: Claim) = {
    val aboutOtherMoney = claim.questionGroup[AboutOtherMoney].getOrElse(AboutOtherMoney())
    val statutorySickPay = claim.questionGroup[StatutorySickPay].getOrElse(StatutorySickPay())
    val otherStatutoryPay = claim.questionGroup[OtherStatutoryPay].getOrElse(OtherStatutoryPay())
    val otherEEAState = claim.questionGroup[OtherEEAStateOrSwitzerland].getOrElse(OtherEEAStateOrSwitzerland())

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

  def sectionEmpty(nodeSeq: NodeSeq) = {
    if (nodeSeq == null || nodeSeq.isEmpty) true else nodeSeq.text.isEmpty
  }

  private def textLine(text: String): Elem = <Evidence>
    <Title></Title>
    <Content>{text}</Content>
  </Evidence>

  private def textLine(label: String, value: String): Elem = {
    <Evidence>
      <Title>{label}</Title>
      <Content>{formatValue(value)}</Content>
    </Evidence>
  }

  private def textLine(label: String, value: Option[String]): Elem = value match {
    case Some(s) => textLine(label, value.getOrElse(""))
    case None => <Evidence/>
  }
}